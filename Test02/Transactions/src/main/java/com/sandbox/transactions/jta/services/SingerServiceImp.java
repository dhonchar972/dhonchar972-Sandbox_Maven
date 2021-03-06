package com.sandbox.transactions.jta.services;

import com.sandbox.transactions.jta.entities.Singer;
import com.sandbox.transactions.jta.exceptions.AsyncXAResourcesException;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service("singerService")
@Repository
@Transactional
@SuppressWarnings("unchecked")
public class SingerServiceImp implements SingerService {
    private static final String FIND_ALL= "select s from Singer s";

    @PersistenceContext(unitName = "emfA")
    private EntityManager emA;
    @PersistenceContext(unitName = "emfB")
    private EntityManager emB;

    @Override
    @Transactional(readOnly = true)
    public List<Singer> findAll() {
        List<Singer> singersFromA = findAllInA();
        List<Singer> singersFromB = findAllInB();
        if (singersFromA.size()!= singersFromB.size()){
            throw new AsyncXAResourcesException("XA resources obj not contain the same expected data");
        }
        Singer sA = singersFromA.get(0);
        Singer sB = singersFromB.get(0);
        if (!sA.getFirstName().equals(sB.getFirstName()))  {
            throw new AsyncXAResourcesException("XA resources obj not contain the same expected data");
        }
        List<Singer> singersFromBoth = new ArrayList<>();
        singersFromBoth.add(sA);
        singersFromBoth.add(sB);
        return singersFromBoth;
    }

    private List<Singer> findAllInA(){
        return emA.createQuery(FIND_ALL).getResultList();
    }

    private List<Singer> findAllInB(){
        return emB.createQuery(FIND_ALL).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Singer findById(Long id){
        throw new NotImplementedException("findByid");
    }

    @Override
    public void save(Singer singer) {
        Singer singerB = new Singer();
        singerB.setFirstName(singer.getFirstName());
        singerB.setLastName(singer.getLastName());
        if (singer.getId() == null) {
            emA.persist(singer);
            emB.persist(singerB);
        } else {
            emA.merge(singer);
            emB.merge(singer);
        }
    }

    @Override
    public long countAll() {
        return 0;
    }
}