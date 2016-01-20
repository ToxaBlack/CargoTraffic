package repository;

import models.User;
import org.apache.commons.collections4.CollectionUtils;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by Anton Chernov on 1/21/2016.
 */
public class UserRepository {
    public User find(long id) {
        EntityManager em = JPA.em();
        return em.find(User.class, id);
    }

    public  User findByName(String name) {
        EntityManager em = JPA.em();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> u = query.from(User.class);
        query.select(u).where(builder.equal(u.get("username"), name));
        TypedQuery<User> q = em.createQuery(query);
        List<User> userList = q.getResultList();
        if (CollectionUtils.isNotEmpty(userList)) return userList.get(0);
        return null;
    }


    public List<User> findAll() {
        EntityManager em = JPA.em();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> from = criteriaQuery.from(User.class);

        CriteriaQuery<User> select = criteriaQuery.select(from);
        TypedQuery<User> q = em.createQuery(select);

        return q.getResultList();
    }

    public void update(User user) {
        EntityManager em = JPA.em();
        em.merge(user);
    }


}
