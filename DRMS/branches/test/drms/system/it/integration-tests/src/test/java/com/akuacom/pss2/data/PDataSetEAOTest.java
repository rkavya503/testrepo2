/**
 * 
 */
package com.akuacom.pss2.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.akuacom.ejb.AbstractVersionedEAOTest;
import com.akuacom.pss2.system.property.CorePropertyEAO;

/**
 * Testing the CRUD methods on the {@link CorePropertyEAO} session bean.
 * 
 * @author roller
 * 
 */
public class PDataSetEAOTest extends
		AbstractVersionedEAOTest<PDataSetEAO, PDataSet> {

	private static PDataSetEAOTest instance;

	public static PDataSetEAOTest getInstance() {
		if (null == instance) {
			instance = new PDataSetEAOTest();
		}
		return instance;
	}

	/**
	 * The default Embedded container test.
	 * 
	 * @see JBossEmbeddedFixture
	 * */
	public PDataSetEAOTest() {
		super(PDataSetEAOBean.class, PDataSetEAO.class, PDataSetEAOBean.class,
				PDataSet.class);
	}

	protected void assertEntityValuesNotEquals(PDataSet created, PDataSet found) {
		assertTrue(found.getPeriod() != created.getPeriod());

	}

	/**
	 * Mutates some property on the entity.
	 * 
	 * @param entity
	 *            entity
	 */
	@Override
	protected void mutate(PDataSet entity) {
		entity.setPeriod(200);
	}

	@Override
	protected void assertEntityValuesEquals(PDataSet created, PDataSet found) {
		assertEquals(created.getPeriod(), found.getPeriod());

	}

	@Override
	protected PDataSet generateRandomEntity() {
		PDataSet dataSet = new PDataSetTest().generateRandomIncompleteEntity();

		assertNotNull(dataSet);
		return dataSet;
	}

}
