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

package de.flapdoodle.mongoom.testlab.datastore;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.bson.types.Code;

import com.google.common.collect.Lists;
import com.google.inject.internal.Sets;

import de.flapdoodle.mongoom.AbstractMongoOMTest;
import de.flapdoodle.mongoom.IDatastore;
import de.flapdoodle.mongoom.IEntityQuery;
import de.flapdoodle.mongoom.IQuery;
import de.flapdoodle.mongoom.IQueryOperation;
import de.flapdoodle.mongoom.datastore.Datastore;
import de.flapdoodle.mongoom.mapping.context.IMappingContext;
import de.flapdoodle.mongoom.mapping.context.IMappingContextFactory;
import de.flapdoodle.mongoom.mapping.context.Transformations;
import de.flapdoodle.mongoom.mapping.properties.Property;
import de.flapdoodle.mongoom.mapping.properties.PropertyReference;
import de.flapdoodle.mongoom.mapping.types.color.Colors;
import de.flapdoodle.mongoom.mapping.types.date.Dates;
import de.flapdoodle.mongoom.testlab.ColorMappingContext;
import de.flapdoodle.mongoom.testlab.DateMappingContext;
import de.flapdoodle.mongoom.testlab.datastore.beans.Article;
import de.flapdoodle.mongoom.testlab.datastore.beans.Book;
import de.flapdoodle.mongoom.testlab.datastore.beans.ColorsBean;
import de.flapdoodle.mongoom.testlab.datastore.beans.NativeTypes;


public class TestDatastore extends AbstractMongoOMTest {

	public void testBooks() {
		Set<Class<?>> classes=Sets.newHashSet();
		classes.add(Book.class);
		IMappingContextFactory<?> factory=new IMappingContextFactory<IMappingContext>() {
			@Override
			public IMappingContext newContext() {
				return new ColorMappingContext();
			}
		};
		Transformations transformations = new Transformations(factory,classes);
		IDatastore datastore=new Datastore(getMongo(), getDatabaseName(), transformations);
		datastore.ensureCaps();
		datastore.ensureIndexes();
		
		Book book = new Book();
		book.setName("Bla");
		datastore.save(book);
		book.setName("Blu");
		datastore.update(book);
		book=new Book();
		book.setName("2. Buch");
		datastore.save(book);
		
		List<Book> books = datastore.find(Book.class);
		assertEquals("Size",2,books.size());
//		System.out.println("Books: "+books);
		
		books=datastore.with(Book.class).field(Book.Name).eq("Blu").result().asList();
		assertEquals("Size",1,books.size());

		for (int i=0;i<10;i++) {
			book=new Book();
			book.setName(i+". Buch");
			datastore.save(book);
		}

		books=datastore.with(Book.class).result().order("name", false).asList();
		System.out.println("Books: "+books);
		assertEquals("Size",9,books.size());
		assertEquals("9. Buch","9. Buch",books.get(0).getName());
	}
	

	public void testColors() {
		Set<Class<?>> classes=Sets.newHashSet();
		classes.add(ColorsBean.class);
		IMappingContextFactory<?> factory=new IMappingContextFactory<IMappingContext>() {
			@Override
			public IMappingContext newContext() {
				return new ColorMappingContext();
			}
		};
		Transformations transformations = new Transformations(factory,classes);
		IDatastore datastore=new Datastore(getMongo(), getDatabaseName(), transformations);
		datastore.ensureCaps();
		datastore.ensureIndexes();
		
		ColorsBean colors = new ColorsBean();
		colors.setColors(Lists.newArrayList(new Color(103,53,23,43),new Color(206,106,56,86)));
		datastore.save(colors);
		colors.setColors(Lists.newArrayList(new Color(100,50,25,44),new Color(200,100,50,88)));
		datastore.update(colors);
		colors=new ColorsBean();
		colors.setColors(Lists.newArrayList(new Color(100,50,25,44)));
		datastore.save(colors);
		
		List<ColorsBean> list= datastore.find(ColorsBean.class);
		assertEquals("Size",2,list.size());
//		System.out.println("Books: "+books);
		
		list=datastore.with(ColorsBean.class).field(ColorsBean.Colors).field(Colors.Red).eq(100).result().asList();
		assertEquals("Size",2,list.size());
		
		list=datastore.with(ColorsBean.class).field(ColorsBean.Colors).field(Colors.Red).eq(200).result().asList();
		assertEquals("Size",1,list.size());
		
		list=datastore.with(ColorsBean.class).field(Property.ref("colors",List.class)).field(Property.ref("r",Integer.class)).eq(200).result().asList();
		assertEquals("Size",1,list.size());
	}

	public void testNativeTypes() {
		Set<Class<?>> classes=Sets.newHashSet();
		classes.add(NativeTypes.class);
		IMappingContextFactory<?> factory=new IMappingContextFactory<IMappingContext>() {
			@Override
			public IMappingContext newContext() {
				return new ColorMappingContext();
			}
		};
		Transformations transformations = new Transformations(factory,classes);
		IDatastore datastore=new Datastore(getMongo(), getDatabaseName(), transformations);
		datastore.ensureCaps();
		datastore.ensureIndexes();
		
		Code a=new Code("{/**/}");
		Code b=new Code("{/**/}");
		assertEquals("Code EQ",a, b);
		
		NativeTypes nt = NativeTypes.withValues();
		NativeTypes nt2 = NativeTypes.withValues();
		assertEquals("Instance EQ",nt, nt2);
		
		datastore.save(nt);
		List<NativeTypes> nts = datastore.find(NativeTypes.class);
		assertEquals("Size",1,nts.size());
		
		NativeTypes read=datastore.with(NativeTypes.class).id().eq(nt.getId()).result().get();
		assertEquals("Read EQ",read, nt);
	}	
	
	public void testArticles() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2011);
		cal.set(Calendar.MONTH, Calendar.FEBRUARY);
		cal.set(Calendar.DAY_OF_MONTH, 4);
		cal.set(Calendar.HOUR, 15);
		cal.set(Calendar.MINUTE, 30);
		cal.set(Calendar.SECOND, 45);
		cal.set(Calendar.MILLISECOND, 0);
		Date date=cal.getTime();
		
		Set<Class<?>> classes=Sets.newLinkedHashSet();
		classes.add(Book.class);
		classes.add(Article.class);
		IMappingContextFactory<?> factory=new IMappingContextFactory<IMappingContext>() {
			@Override
			public IMappingContext newContext() {
				return new DateMappingContext();
			}
		};
		Transformations transformations = new Transformations(factory,classes);
		IDatastore datastore=new Datastore(getMongo(), getDatabaseName(), transformations);
		datastore.ensureCaps();
		datastore.ensureIndexes();
		
		Article article = new Article();
		article.setName("Bla");
		datastore.save(article);
		article.setName("Blu");
		datastore.update(article);
		article=new Article();
		article.setName("2. Buch");
		article.setCreated(date);
		datastore.save(article);
		
		List<Article> articles = datastore.find(Article.class);
		assertEquals("Size",2,articles.size());
//		System.out.println("Books: "+books);
		
		articles=datastore.with(Article.class).field(Article.Created).field(Dates.Year).eq(2011).result().asList();
		assertEquals("Size",1,articles.size());
		articles=datastore.with(Article.class).field(Article.Created).field(Dates.Year).eq(2010).result().asList();
		assertEquals("Size",0,articles.size());
		articles=datastore.with(Article.class).field(Article.Created).field(Dates.Year).gt(2010).result().asList();
		assertEquals("Size",1,articles.size());

	}

}
