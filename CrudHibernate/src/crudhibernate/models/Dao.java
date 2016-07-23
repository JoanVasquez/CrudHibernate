package crudhibernate.models;

import crudhibernate.interfaces.CrudMethods;
import crudhibernate.util.HibernateUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author <h1>Joan Im</h1>
 * @version 1.0
 * <p>
 * This class is going to be used for the <b>Dao</b> process, it is not a big
 * lib but it is going to be user for the principal process. It has method for
 * inserting, updating, deleting, get by ID, get by Like, get total rows, get
 * entities, login, forgotten password.</p>
 *
 * @param <T> <b>Entity</b> <span>The param T is going to be the Entity which
 * represents the kind of POJO to be persisted.</span>
 */
public class Dao<T> implements CrudMethods<T> {

    private final SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;
    private Criteria criteria;

    /**
     * <p>
     * Constructor used to initialized the sessionFactory.</p>
     * <span>Only Constructor for this class</span>
     */
    public Dao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    /**
     * @param entity <span>Entity to be persisted.</span>
     * @return <span>true or false to indicate if the POJO was persisted.</span>
     * @see T
     */
    //1 - Get the session in which we're working.
    //2 - Begging the transaction.
    //3 - Process the entity.
    //4 - Make the commit.
    @Override
    public boolean insert(T entity) {
        session = sessionFactory.getCurrentSession();
        try {
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
            return true;
        } catch (HibernateException ex) {
            transaction.rollback();
            System.err.println("Exception: " + ex);
        } finally {
            if (!sessionFactory.isClosed()) {
                sessionFactory.close();
            }
        }
        return false;
    }

    /**
     *
     * @param entity <span>Entity to be updated.</span>
     * @return <span>true or false to indicate if the POJO was updated.</span>
     * @see T
     */
    //1 - Get the session in which we're working.
    //2 - Begging the transaction.
    //3 - Process the entity.
    //4 - Make the commit.
    @Override
    public boolean update(T entity) {
        session = sessionFactory.getCurrentSession();
        try {
            transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
            return true;
        } catch (HibernateException ex) {
            transaction.rollback();
            System.err.println("Exception: " + ex);
        } finally {
            if (!sessionFactory.isClosed()) {
                sessionFactory.close();
            }
        }

        return false;
    }

    /**
     *
     * @param className <span>Entity to be deleted.</span>
     * @param id <span>Id of the entity which is gonna be deleted</span>
     * @return <span>true or false to indicate if the POJO was deleted.</span>
     */
    //1 - Get the session in which we're working.
    //2 - Begging the transaction.
    //3 - Get the entity.
    //4 - Process the entity.
    //5 - Make the commit.
    @Override
    public boolean delete(String className, int id) {
        session = sessionFactory.getCurrentSession();
        try {
            transaction = session.beginTransaction();
            T t = (T) session.get(className, id);
            if (t != null) {
                session.delete(t);
                transaction.commit();
                return true;
            }
        } catch (HibernateException ex) {
            transaction.rollback();
            System.out.println("Exception: " + ex);
        } finally {
            if (!sessionFactory.isClosed()) {
                sessionFactory.close();
            }
        }
        return false;
    }

    /**
     *
     * @param className <span>Entity to be retrieved.</span>
     * @param id <span>Id of the entity which is gonna be retrieved</span>
     * @return <span>The retrieved entity.</span>
     */
    //1 - Get the session in which we're working.
    //2 - Begging the transaction.
    //3 - Get the entity.
    //4 - Make the commit.
    @Override
    public T getById(String className, int id) {
        session = sessionFactory.getCurrentSession();
        T t = null;
        try {
            transaction = session.beginTransaction();
            t = (T) session.get(className, id);
            transaction.commit();
        } catch (HibernateException ex) {
            System.out.println("Exception: " + ex);
        } finally {
            if (!sessionFactory.isClosed()) {
                sessionFactory.close();
            }
        }

        return t;
    }

    /**
     *
     * @param className <span>Entity to be retrivied.</span>
     * @param columnName <span>Name of the column in where the LIKE CLAUSE is
     * gonna be applied.</span>
     * @param valueToFilter <span>The LIKE CLAUSE to be applied: %abc, %abc%,
     * abc%</span>
     * @param pageNumber <span>Page number for the pagination. Page where we're
     * located</span>
     * @param rowPerPage <span>Row per page for the pagination. How many rows to
     * display</span>
     * @return <span>The retrieved entity.</span>
     */
    //1 - Get the session in which we're working.
    //2 - Begging the transaction.
    //3 - Create the criteria to apply the LIKE CLAUSE.
    //4 - Add the LIKE CLAUSE
    //5 - Apply the pagination.
    //6 - Get the result.
    //7 - Make the commit.
    @Override
    public List<T> getByLike(String className, String columnName, String valueToFilter, int pageNumber, int rowPerPage) {
        session = sessionFactory.getCurrentSession();
        List<T> entities = null;
        try {
            transaction = session.beginTransaction();
            criteria = session.createCriteria(className);
            criteria.add(Restrictions.like(columnName, valueToFilter));
            criteria.setFirstResult(pageNumber);
            criteria.setMaxResults(rowPerPage);
            entities = criteria.list();
            transaction.commit();
        } catch (HibernateException ex) {
            System.out.println("Exception: " + ex);
        } finally {
            if (!sessionFactory.isClosed()) {
                sessionFactory.close();
            }
        }

        return entities;
    }

    /**
     *
     * @param className <span>Entity to be retrivied.</span>
     * @return <span>The total of rows.</span>
     */
    //1 - Get the session in which we're working.
    //2 - Begging the transaction.
    //3 - Create the criteria to get the total rows.
    //4 - Get the result.
    //5 - Make the commit.
    @Override
    public Long getTotalRows(String className) {
        session = sessionFactory.getCurrentSession();
        Long totalRows = 0L;
        try {
            transaction = session.beginTransaction();
            criteria = session.createCriteria(className);
            criteria.setProjection(Projections.rowCount());
            totalRows = (Long) criteria.uniqueResult();
            transaction.commit();
        } catch (HibernateException ex) {
            System.out.println("Exception: " + ex);
        } finally {
            if (!sessionFactory.isClosed()) {
                sessionFactory.close();
            }
        }

        return totalRows;
    }

    /**
     *
     * @param className <span>Entities to be retrivied.</span>
     * @param pageNumber <span>Page number for the pagination. Page where we're
     * located</span>
     * @param rowPerPage <span>Row per page for the pagination. How many rows to
     * display</span>
     * @return <span>The retrieved entities.</span>
     */
    //1 - Get the session in which we're working.
    //2 - Begging the transaction.
    //3 - Create the criteria to apply the pagination.
    //4 - Get the result.
    //5 - Make the commit.
    @Override
    public List<T> getEntities(String className, int pageNumber, int rowPerPage) {
        session = sessionFactory.getCurrentSession();
        List<T> entities = null;
        try {
            transaction = session.beginTransaction();
            criteria = session.createCriteria(className);
            criteria.setFirstResult(pageNumber);
            criteria.setMaxResults(rowPerPage);
            entities = criteria.list();
            transaction.commit();
        } catch (HibernateException e) {
            System.out.println("Exception: " + e);
        } finally {
            if (!sessionFactory.isClosed()) {
                sessionFactory.close();
            }
        }

        return entities;
    }

    /**
     *
     * @param className <span>Entity to be retrivied.</span>
     * @param email <span>Email of the user</span>
     * @param password <span>Password of the user</span>
     * @return <span>The retrieved entity.</span>
     */
    //1 - Get the session in which we're working.
    //2 - Begging the transaction.
    //3 - Create the criteria to apply the logical expression.
    //4 - Create the criterion with the logical expresions.
    //5 - Load the restrictions with the clause and.
    //6 - Get the result.
    //7 - Make the commit.
    @Override
    public T login(String className, String email, String password) {
        session = sessionFactory.getCurrentSession();
        T t = null;
        try {
            transaction = session.beginTransaction();
            criteria = session.createCriteria(className);
            Criterion emailCriterion = Restrictions.eq("email", email);
            Criterion passwordCriterion = Restrictions.eq("password", password);
            LogicalExpression logicalExpression = Restrictions.and(emailCriterion, passwordCriterion);
            criteria.add(logicalExpression);
            t = (T) criteria.uniqueResult();
            transaction.commit();
        } catch (HibernateException e) {
            System.out.println("Exception: " + e);
        } finally {
            if (!sessionFactory.isClosed()) {
                sessionFactory.close();
            }
        }
        return t;
    }

    /**
     *
     * @param className <span>Entity to be retrivied.</span>
     * @param email <span>Email of the user</span>
     * @return <span>The retrieved entity.</span>
     */
    //1 - Get the session in which we're working.
    //2 - Begging the transaction.
    //3 - Create the criteria to apply the logical expression.
    //4 - Get the result.
    //5 - Make the commit.
    @Override
    public T forgottenPassword(String className, String email) {
        session = sessionFactory.getCurrentSession();
        T t = null;
        try {
            transaction = session.beginTransaction();
            criteria = session.createCriteria(className);
            criteria.add(Restrictions.eq("email", email));
            t = (T) criteria.uniqueResult();
            transaction.commit();
        } catch (HibernateException e) {
            System.out.println("Exception: " + e);
        } finally {
            if (!sessionFactory.isClosed()) {
                sessionFactory.close();
            }
        }
        return t;
    }

}
