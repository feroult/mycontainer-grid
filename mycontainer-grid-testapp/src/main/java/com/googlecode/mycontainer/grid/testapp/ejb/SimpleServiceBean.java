package com.googlecode.mycontainer.grid.testapp.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.googlecode.mycontainer.annotation.MycontainerLocalBinding;

@Stateless
@MycontainerLocalBinding(value = SimpleService.JNDI_LOCAL)
public class SimpleServiceBean implements SimpleService {

	@PersistenceContext(unitName = "test-pu")
	private EntityManager em;

	@Override
	public String getHelloWorld() {
		return "hello world";
	}

	@Override
	public SimpleEntity create(String mensagem) {
		SimpleEntity entity = new SimpleEntity();
		entity.setMensagem(mensagem);

		em.persist(entity);
		return entity;
	}

	@Override
	public SimpleEntity findById(Long id) {
		return em.find(SimpleEntity.class, id);
	}

}
