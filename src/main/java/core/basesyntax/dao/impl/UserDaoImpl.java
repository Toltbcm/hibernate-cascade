package core.basesyntax.dao.impl;

import core.basesyntax.dao.UserDao;
import core.basesyntax.model.User;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class UserDaoImpl extends AbstractDao implements UserDao {
    public UserDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    @Override
    public User create(User entity) {
        Session session = null;
        try {
            session = factory.openSession();
            session.save(entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Can't create new user: " + entity, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    @Override
    public User get(Long id) {
        try (Session session = factory.openSession()) {
            return session.find(User.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Can't get user by ID: " + id, e);
        }
    }
    
    @Override
    public List<User> getAll() {
        try (Session session = factory.openSession()) {
            return session.createQuery("from User", User.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Can't get all users.", e);
        }
    }
    
    @Override
    public void remove(User entity) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't delete the user: " + entity, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
