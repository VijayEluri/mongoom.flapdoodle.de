/**
 * Copyright (C) 2010 Michael Mosmann <michael@mosmann.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.flapdoodle.mongoom.mapping.converter;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import de.flapdoodle.mongoom.annotations.ConverterType;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.Transient;
import de.flapdoodle.mongoom.annotations.Version;
import de.flapdoodle.mongoom.annotations.index.IndexGroup;
import de.flapdoodle.mongoom.annotations.index.IndexGroups;
import de.flapdoodle.mongoom.annotations.index.IndexOption;
import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.logging.LogConfig;
import de.flapdoodle.mongoom.mapping.ITypeConverter;
import de.flapdoodle.mongoom.mapping.IVersionFactory;
import de.flapdoodle.mongoom.mapping.Mapper;
import de.flapdoodle.mongoom.mapping.MappingContext;
import de.flapdoodle.mongoom.mapping.converter.reflection.ClassInformation;
import de.flapdoodle.mongoom.mapping.index.FieldIndex;
import de.flapdoodle.mongoom.mapping.index.IndexDef;

public abstract class AbstractObjectConverter<T> extends AbstractReadOnlyConverter<T>
{
	private static final Logger _logger = LogConfig.getLogger(AbstractObjectConverter.class);
	
	private final Set<MappedAttribute> _attributes;
	private final MappedAttribute _idAttribute;
	private final MappedAttribute _version;
	private final IVersionFactory _versionFactory;
	
	private final List<IndexDef> _indexes;

//	private final Class<T> _entityClass;
//	private final Constructor<T> _constructor;
	
	protected AbstractObjectConverter(Mapper mapper, MappingContext<?> context,Class<T> entityClass)
	{
//		_entityClass = entityClass;
//		_constructor = ClassAndFields.getConstructor(_entityClass);
		super(mapper,context,entityClass);
		
		_attributes=Sets.newLinkedHashSet();
		
		Map<String, EntityIndexDef> indexGroupMap = getIndexGroupMap(entityClass);
		List<IndexDef> indexDefinitions=Lists.newArrayList();
		if (!isEntity() && (!indexDefinitions.isEmpty())) throw new MappingException(entityClass,"Use IndexGroup Definitions only on Entities");
		
		boolean idSet=false;
		MappedAttribute idAttr=null;
		MappedAttribute versionAttr=null;
		IVersionFactory<?> versionFactory=null;
		
		List<Field> fields = ClassInformation.getFields(entityClass);
		for (Field f : fields)
		{
			f.setAccessible(true);
			if (f.getAnnotation(Transient.class)==null)
			{
//				String fieldName=f.getName();
//				Property pAnn = f.getAnnotation(Property.class);
				Id idAnn = f.getAnnotation(Id.class);
				Indexed idxAnn = f.getAnnotation(Indexed.class);
				IndexedInGroup idxInGAnn = f.getAnnotation(IndexedInGroup.class);
				
				boolean isVersioned=f.getAnnotation(Version.class)!=null;
				if ((isVersioned) && (!isEntity())) throw new MappingException(entityClass,"Version only supported in EntityClass");
				
				if (idAnn!=null)
				{
					if (!isEntity()) throw new MappingException(entityClass,"Id only allowed in entity");
					if (idSet) throw new MappingException(entityClass,"More then one Id");
					idSet=true;
					
//					fieldName=Const.ID_FIELDNAME;
//					if (pAnn!=null) throw new MappingException(entityClass,"Id and Property in same place");
					if (idxAnn!=null) throw new MappingException(entityClass,"Id and Indexed in same place");
					if (idxInGAnn!=null) throw new MappingException(entityClass,"Id and IndexedInGroup in same place");
				}
				String fieldName=mapper.getFieldName(f);
				if (fieldName==null)
				{
					throw new MappingException(entityClass,"FieldName is NULL: "+f);
				}
				else
				{
					if (fieldName.contains(".")) throw new MappingException(entityClass,"Fieldnames should not contain '.'.");
				}
//				if (pAnn!=null)
//				{
//					fieldName=pAnn.value();
//				}
				
				_logger.severe("Map "+entityClass+" Field "+f+" -> "+f.getType()+":"+f.getGenericType());
				ITypeConverter fieldConverter = mapper.map(context,f.getType(),f.getGenericType(), f.getAnnotation(ConverterType.class));
				if (isVersioned)
				{
					versionFactory = mapper.getVersionFactory(f.getType());
					if (versionFactory==null) throw new MappingException(entityClass,"VersionFactory not found: "+f);
				}
				
				MappedAttribute mappedAttribute = new MappedAttribute(f,fieldName,fieldConverter);
				if (!_attributes.add(mappedAttribute))
				{
					throw new MappingException(entityClass,"Field with name "+fieldName+" allready defined");
				}
				if (idAnn!=null) idAttr=mappedAttribute;
				if (isVersioned) versionAttr=mappedAttribute;
				
				if ((idxAnn!=null) && (idxInGAnn!=null)) throw new MappingException(entityClass,"Field "+f+" is indexed and indexedInGroup");
				
				if (idxAnn!=null)
				{
					List<IndexDef> subIndexes = fieldConverter.getIndexes();
					if ((subIndexes!=null) && (!subIndexes.isEmpty())) throw new MappingException(entityClass,"Field "+f+" is indexed, but type has index too");
					
					IndexOption options = idxAnn.options();
					String name = idxAnn.name();
					if (name.length()==0) name=null;
					indexDefinitions.add(new IndexDef(name, Lists.newArrayList(new FieldIndex(fieldName,idxAnn.direction())), options.unique(), options.dropDups()));
				}
				else
				{
					if (idxInGAnn!=null)
					{
						String groupName = idxInGAnn.group();
						if (isEntity())
						{
							EntityIndexDef def = indexGroupMap.get(groupName);
							if (def==null) throw new MappingException(entityClass,"Field "+f+", IndexGroup "+groupName+" not defined");
							def.addField(new FieldIndex(fieldName,idxInGAnn.direction()));
						}
						else
						{
							indexDefinitions.add(new IndexDef(groupName, new FieldIndex(fieldName,idxInGAnn.direction())));
						}
					}
					else
					{
						List<IndexDef> subIndexes = fieldConverter.getIndexes();
						if (subIndexes!=null)
						{
							for (IndexDef def : subIndexes)
							{
								if (def.group()!=null)
								{
									EntityIndexDef gdef = indexGroupMap.get(def.group());
									if (gdef==null) throw new MappingException(entityClass,"Field "+f+", IndexGroup "+def.group()+" not defined (in FieldType)");
									FieldIndex field = def.fields().get(0);
									gdef.addField(new FieldIndex(fieldName+"."+field.name(),field.direction()));
								}
								else
								{
									List<FieldIndex> indexFields=Lists.newArrayList();
									for (FieldIndex fi : def.fields())
									{
										indexFields.add(new FieldIndex(fieldName+"."+fi.name(),fi.direction()));
									}
									indexDefinitions.add(new IndexDef(def.name(),indexFields,def.unique(),def.dropDups()));
								}
							}
						}
					}
				}
			}
		}
		
		indexDefinitions.addAll(indexGroupMap.values());
		_indexes=indexDefinitions;
		_idAttribute=idAttr;
		_version=versionAttr;
		_versionFactory=versionFactory;
		
		if ((!idSet) && (isEntity())) throw new MappingException(entityClass,"Id not found");
		
	}

	protected boolean isEntity()
	{
		return false;
	}
	
	protected MappedAttribute getIdAttribute()
	{
		return _idAttribute;
	}
	
	public List<IndexDef> getIndexes()
	{
		return _indexes;
	}
	
	protected T convertDBObjectToEntity(DBObject dbobject)
	{
		T ret=newInstance();
		for (MappedAttribute a : _attributes)
		{
			setEntityField(ret, a, dbobject);
		}
		return ret;
	}
	
	protected DBObject convertEntityToDBObject(T entity)
	{
		return convertToDBObject(entity, _attributes);
	}

	protected DBObject convertEntityToKeyDBObject(T entity)
	{
		if (_version!=null)	return convertToDBObject(entity, Sets.newHashSet(_idAttribute,_version));
		return convertToDBObject(entity, Sets.newHashSet(_idAttribute));
	}
	
	public void newVersion(T entity)
	{
		if (!getEntityClass().isInstance(entity)) throw new MappingException(getEntityClass(),"Is not Instance of "+getEntityClass()+": "+entity.getClass()+"("+entity+")");
		if (_version!=null)
		{
			Field field = _version.getField();
			try
			{
				field.set(entity, _versionFactory.newVersion(field.get(entity)));
			}
			catch (IllegalArgumentException e)
			{
				throw new MappingException(getEntityClass(),e);
			}
			catch (IllegalAccessException e)
			{
				throw new MappingException(getEntityClass(),e);
			}
		}
	};

	private DBObject convertToDBObject(T entity, Set<MappedAttribute> attributes)
	{
		if (!getEntityClass().isInstance(entity)) throw new MappingException(getEntityClass(),"Is not Instance of "+getEntityClass()+": "+entity.getClass()+"("+entity+")");
		try
		{
			BasicDBObject ret=new BasicDBObject();
			for (MappedAttribute a : attributes)
			{
				Field field = a.getField();
				Object fieldValue = field.get(entity);
				if (fieldValue!=null)
				{
					ITypeConverter converter = a.getConverter();
					ret.put(a.getName(), converter.convertTo(fieldValue));
				}
			}
			return ret;
		}
		catch (IllegalArgumentException e)
		{
			throw new MappingException(getEntityClass(),e);
		}
		catch (IllegalAccessException e)
		{
			throw new MappingException(getEntityClass(),e);
		}
	};
	
	public ITypeConverter<?> converter(String field)
	{
		String name=field;
		String left=null;
		int idx=field.indexOf(".");
		if (idx!=-1)
		{
			name=field.substring(0,idx);
			left=field.substring(idx+1);
		}
		
		MappedAttribute m=getAttribute(name);
		ITypeConverter<?> ret = m.getConverter();
		if (left!=null)
		{
			ret=ret.converter(left);
		}
		return ret;
	}

	protected MappedAttribute getAttribute(String name)
	{
		for (MappedAttribute m : _attributes)
		{
			if (m.getName().equals(name)) return m;
		}
		throw new MappingException(getEntityClass(),"Field "+name+" not defined");
	}
	
	private static Map<String, EntityIndexDef> getIndexGroupMap(Class<?> entityClass)
	{
		Map<String,EntityIndexDef> map=Maps.newHashMap();
		IndexGroup[] indexGroups = getIndexGroups(entityClass);
		for (IndexGroup ig : indexGroups)
		{
			IndexOption options = ig.options();
			String name = ig.name();
			if (name.length()==0) name=null;
			map.put(ig.group(),new EntityIndexDef(name,options.unique(),options.dropDups()));
		}
		return map;
	}


	private static IndexGroup[] getIndexGroups(Class<?> entityClass)
	{
		IndexGroup[] list={};
		IndexGroups indexGroups = entityClass.getAnnotation(IndexGroups.class);
		if (indexGroups!=null)
		{
			list = indexGroups.value();
		}
		else
		{
			IndexGroup indexGroup = entityClass.getAnnotation(IndexGroup.class);
			if (indexGroup!=null)
			{
				list=new IndexGroup[]{indexGroup};
			}
		}
		return list;
	}
	
	static class EntityIndexDef extends IndexDef
	{
		public EntityIndexDef(String name, boolean unique, boolean dropDups)
		{
			super(name, Lists.<FieldIndex>newArrayList(), unique, dropDups);
		}
		
		protected void addField(FieldIndex field)
		{
			_fields.add(field);
		}
	}

	public boolean matchType(Class<?> entityClass,Class<?> type, Type genericType)
	{
		return type.isAssignableFrom(getEntityClass());
	}

}
