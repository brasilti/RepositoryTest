package br.com.wave.project.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.wave.project.core.entities.EntidadeBasic;
import br.com.wave.repository.core.Keeper;
import br.com.wave.repository.enums.RemoveEnum;
import br.com.wave.repository.exceptions.RepositoryException;

public class KeeperTest {
	
	private Keeper keeper;

	private EntityManager manager;

	private EntityTransaction transaction;

	@Before
	public void setUp() {
		WeldContainer container = new Weld().initialize();
		this.keeper = container.instance().select(Keeper.class).get();

		this.manager = container.instance().select(EntityManager.class).get();
		this.transaction = this.manager.getTransaction();
		this.transaction.begin();
	}

	@Test
	public void devePersistirUmaInstanciaEmUmRepositorio() throws RepositoryException {
		EntidadeBasic instance = new EntidadeBasic();

		this.keeper.persist(instance);

		assertNotNull(instance.getId());
		assertTrue(this.manager.contains(instance));
	}

	@Test
	public void deveAlterarUmaInstanciaEmUmRepositorio() throws RepositoryException {
		EntidadeBasic instance = new EntidadeBasic();

		this.keeper.persist(instance);
		assertNotNull(instance.getId());
		assertTrue(this.manager.contains(instance));

		this.manager.detach(instance);
		assertFalse(this.manager.contains(instance));

		String value = "Teste";
		instance.setStringField(value);

		this.keeper.persist(instance);

		EntidadeBasic actualInstance = this.manager.find(EntidadeBasic.class, instance.getId());
		assertEquals(value, actualInstance.getStringField());
	}

	@Test
	public void deveRemoverDeFormaLogicaUmaInstanciaEmUmRepositorio() throws RepositoryException {
		EntidadeBasic instance = new EntidadeBasic();

		this.keeper.persist(instance);
		assertNotNull(instance.getId());
		assertTrue(this.manager.contains(instance));

		this.keeper.remove(instance);

		assertTrue(this.manager.contains(instance));
		assertNotNull(instance.getId());
		assertFalse(instance.getActive());
	}

	@Test
	public void deveRemoverDeFormaLogicaUmaInstanciaDetachedEmUmRepositorio() throws RepositoryException {
		EntidadeBasic instance = new EntidadeBasic();

		this.keeper.persist(instance);
		assertNotNull(instance.getId());
		assertTrue(this.manager.contains(instance));

		this.manager.detach(instance);
		assertFalse(this.manager.contains(instance));

		this.keeper.remove(instance);

		assertNotNull(instance.getId());
		assertFalse(instance.getActive());
	}

	@Test
	public void deveRemoverDeFormaLogicaComParametroUmaInstanciaEmUmRepositorio() throws RepositoryException {
		EntidadeBasic instance = new EntidadeBasic();

		this.keeper.persist(instance);
		assertNotNull(instance.getId());
		assertTrue(this.manager.contains(instance));

		this.keeper.remove(instance, RemoveEnum.LOGICAL);

		assertTrue(this.manager.contains(instance));
		assertNotNull(instance.getId());
		assertFalse(instance.getActive());
	}

	@Test
	public void deveRemoverDeFormaLogicaComParametroUmaInstanciaDetachedEmUmRepositorio() throws RepositoryException {
		EntidadeBasic instance = new EntidadeBasic();

		this.keeper.persist(instance);
		assertNotNull(instance.getId());
		assertTrue(this.manager.contains(instance));

		this.manager.detach(instance);
		assertFalse(this.manager.contains(instance));

		this.keeper.remove(instance, RemoveEnum.LOGICAL);

		assertNotNull(instance.getId());
		assertFalse(instance.getActive());
	}

	@Test
	public void deveRemoverDeFormaFisicaUmaInstanciaEmUmRepositorio() throws RepositoryException {
		EntidadeBasic instance = new EntidadeBasic();

		this.keeper.persist(instance);
		assertNotNull(instance.getId());
		assertTrue(this.manager.contains(instance));

		this.keeper.remove(instance, RemoveEnum.PHYSICAL);

		assertFalse(this.manager.contains(instance));

		EntidadeBasic actualInstance = this.manager.find(EntidadeBasic.class, instance.getId());
		assertNull(actualInstance);
	}

	@Test
	public void deveRemoverDeFormaFisicaUmaInstanciaDetachedEmUmRepositorio() throws RepositoryException {
		EntidadeBasic instance = new EntidadeBasic();

		this.keeper.persist(instance);
		assertNotNull(instance.getId());
		assertTrue(this.manager.contains(instance));

		this.manager.detach(instance);
		assertFalse(this.manager.contains(instance));

		this.keeper.remove(instance, RemoveEnum.PHYSICAL);

		EntidadeBasic actualInstance = this.manager.find(EntidadeBasic.class, instance.getId());
		assertNull(actualInstance);
	}

	@After
	public void tearDown() {
		this.transaction.rollback();
		this.transaction = null;
	}

}
