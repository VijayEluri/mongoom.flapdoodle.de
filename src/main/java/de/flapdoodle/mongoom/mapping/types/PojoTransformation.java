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

package de.flapdoodle.mongoom.mapping.types;

import com.mongodb.DBObject;

import de.flapdoodle.mongoom.mapping.ITransformation;
import de.flapdoodle.mongoom.mapping.entities.AbstractBeanTransformation;


public class PojoTransformation<Bean> extends AbstractBeanTransformation<Bean, PojoContext<Bean>> implements ITransformation<Bean, DBObject> {

	
	private final PojoContext<Bean> _pojoContext;

	public PojoTransformation(PojoContext<Bean> context) {
		super(context);
		_pojoContext = context;
		// TODO Auto-generated constructor stub
	}
	
//	@Override
//	public DBObject asObject(Bean value) {
//		if (value==null) return null;
//		
//		BasicDBObject ret = new BasicDBObject();
//		Map<IProperty<?>, ITransformation<?, ?>> propertyTransformations = _pojoContext.getPropertyTransformation();
//		
//		for (IProperty<?> prop : propertyTransformations.keySet()) {
//			IPropertyField<?> p=(IPropertyField<?>) prop;
//			ITransformation transformation = propertyTransformations.get(p);
//			Field field = p.getField();
//			Object fieldValue=getFieldValue(field,value);
//			Object dbValue = transformation.asObject(fieldValue);
//			if (dbValue!=null) ret.put(p.getName(), dbValue);
//		}
//		return ret;
//	}

//	private Object getFieldValue(Field field, Bean value) {
//		try {
//			field.setAccessible(true);
//			return field.get(value);
//		} catch (IllegalArgumentException e) {
//			throw new MappingException(_pojoContext.getBeanClass(),e);
//		} catch (IllegalAccessException e) {
//			throw new MappingException(_pojoContext.getBeanClass(),e);
//		}
//	}
//
//	@Override
//	public Bean asEntity(DBObject object) {
//		if (object==null) return null;
//		
//		Bean ret = newInstance();
//		Map<IProperty<?>, ITransformation<?, ?>> propertyTransformations = _pojoContext.getPropertyTransformation();
//		
//		for (IProperty prop : propertyTransformations.keySet()) {
//			IPropertyField<?> p=(IPropertyField<?>) prop;
//			ITransformation transformation = propertyTransformations.get(p);
//			Field field = p.getField();
//			Object fieldValue=transformation.asEntity(object.get(p.getName()));
//			if (fieldValue!=null) setFieldValue(ret, field, fieldValue);
//		}
//		
//		return ret;
//	}
//
//	private void setFieldValue(Bean bean, Field field, Object fieldValue) {
//		try {
//			field.setAccessible(true);
//			field.set(bean, fieldValue);
//		} catch (IllegalArgumentException e) {
//			throw new MappingException(_pojoContext.getBeanClass(),e);
//		} catch (IllegalAccessException e) {
//			throw new MappingException(_pojoContext.getBeanClass(),e);
//		}
//	}

//	private Bean newInstance() {
//		try {
//			return _pojoContext.getBeanClass().newInstance();
//		} catch (InstantiationException e) {
//			throw new MappingException(_pojoContext.getBeanClass(),e);
//		} catch (IllegalAccessException e) {
//			throw new MappingException(_pojoContext.getBeanClass(),e);
//		}
//	}

//	@Override
//	public <Source> ITransformation<Source, ?> propertyTransformation(TypedPropertyName<Source> property) {
//		throw new MappingException("not implemented");
//	}
//	
//	@Override
//	public ITransformation<?, ?> propertyTransformation(String property) {
//		throw new MappingException("not implemented");
//	}

//	@Override
//	public Set<TypedPropertyName<?>> properties() {
//		return null;
//	}
	
}
