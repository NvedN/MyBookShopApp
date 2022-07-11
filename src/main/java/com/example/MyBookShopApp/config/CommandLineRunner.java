package com.example.MyBookShopApp.config;

import com.example.MyBookShopApp.data.TestEntity;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
public class CommandLineRunner implements org.springframework.boot.CommandLineRunner
{

		EntityManagerFactory entityManagerFactory;

		public CommandLineRunner(EntityManagerFactory entityManagerFactory)
		{
				this.entityManagerFactory = entityManagerFactory;
		}

		@Override public void run(String... args) throws Exception
		{
				for (int i = 0; i < 5; i++)
				{
						createTestEntity(new TestEntity());
				}
				TestEntity readTestEntity = readTestEntityById(3L);
				if (readTestEntity != null)
				{
						Logger.getLogger(CommandLineRunner.class.getSimpleName()).info(readTestEntity.toString());
						System.out.println("----------readTest entity  = " + readTestEntity.toString());
				}
				else
				{
						throw new Exception();
				}
				TestEntity updateTestEntity = updateTestEntityById(5L);
				if (updateTestEntity != null)
				{
						Logger.getLogger(CommandLineRunner.class.getSimpleName()).info("update " + updateTestEntity.toString());
						System.out.println("----------updateTestEntity entity  = " + updateTestEntity.toString());
				}
				else
				{
						throw new Exception();
				}
				deleteTestEnriry(4L);
		}

		private void deleteTestEnriry(Long id)
		{
				Session session = entityManagerFactory.createEntityManager().unwrap((Session.class));
				Transaction tx = null;
				try
				{
						tx = session.beginTransaction();
						TestEntity findEntity = readTestEntityById(id);
						TestEntity mergedTesEntity = (TestEntity) session.merge(findEntity);
						session.remove(mergedTesEntity);
						tx.commit();
				}
				catch (HibernateException hex)
				{
						if (tx != null)
						{
								tx.rollback();
						}
						else
						{
								hex.printStackTrace();
						}
				}
				finally
				{
						session.close();
				}
		}

		private TestEntity updateTestEntityById(Long id)
		{
				Session session = entityManagerFactory.createEntityManager().unwrap((Session.class));
				Transaction tx = null;
				TestEntity result = null;
				try
				{
						tx = session.beginTransaction();
						TestEntity findEntity = readTestEntityById(id);
						findEntity.setData("NEW DATA UPDATE");
						result = (TestEntity) session.merge(findEntity);
						tx.commit();
				}
				catch (HibernateException hex)
				{
						if (tx != null)
						{
								tx.rollback();
						}
						else
						{
								hex.printStackTrace();
						}
				}
				return result;
		}

		private TestEntity readTestEntityById(Long id)
		{
				Session session = entityManagerFactory.createEntityManager().unwrap((Session.class));
				Transaction tx = null;
				TestEntity result = null;
				try
				{
						tx = session.beginTransaction();
						result = session.find(TestEntity.class, id);
						tx.commit();
				}
				catch (HibernateException hex)
				{
						if (tx != null)
						{
								tx.rollback();
						}
						else
						{
								hex.printStackTrace();
						}
				}
				return result;
		}

		private void createTestEntity(TestEntity entity)
		{
				Session session = entityManagerFactory.createEntityManager().unwrap((Session.class));
				Transaction tx = null;
				try
				{
						tx = session.beginTransaction();
						entity.setData(entity.getClass().getSimpleName() + entity.hashCode());
						session.save(entity);
						tx.commit();
				}
				catch (HibernateException hex)
				{
						if (tx != null)
						{
								tx.rollback();
						}
						else
						{
								hex.printStackTrace();
						}
				}
		}
}
