/**
 * 
 */
package com.akuacom.pss2.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.akuacom.ejb.AbstractBaseEAOTest;
import com.akuacom.pss2.system.property.CorePropertyEAO;

/**
 * Testing the CRUD methods on the {@link CorePropertyEAO} session bean.
 * 
 * @author roller
 * 
 */
public class PDataSourceEAOTest extends
		AbstractBaseEAOTest<PDataSourceEAO, PDataSource> {

	private static PDataSourceEAOTest instance;

	public static PDataSourceEAOTest getInstance() {
		if (null == instance) {
			instance = new PDataSourceEAOTest();
		}
		return instance;
	}

	/**
	 * The default Embedded container test.
	 * 
	 * @see JBossEmbeddedFixture
	 * */
	public PDataSourceEAOTest() {
		super(PDataSourceEAOBean.class, PDataSourceEAO.class,
				PDataSourceEAOBean.class, PDataSource.class);
	}

	protected void assertEntityValuesNotEquals(PDataSource created,
			PDataSource found) {
		assertTrue(!found.getName().equals(created.getName()));

	}

	/**
	 * Mutates some property on the entity.
	 * 
	 * @param entity
	 *            entity
	 */
	@Override
	protected void mutate(PDataSource entity) {
		entity.setName("meter2");
	}

	@Override
	protected void assertEntityValuesEquals(PDataSource created,
			PDataSource found) {
		assertEquals(created.getName(), found.getName());

	}

	@Override
	protected PDataSource generateRandomEntity() {
		PDataSource dataSource = new PDataSourceTest().generateRandomEntity();

		assertNotNull(dataSource);
		return dataSource;
	}

}
