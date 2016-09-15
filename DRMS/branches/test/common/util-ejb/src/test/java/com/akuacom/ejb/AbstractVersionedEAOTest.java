package com.akuacom.ejb;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class AbstractVersionedEAOTest
        <EntityEAOType extends BaseEAO<EntityType>, EntityType extends VersionedEntity>
        extends AbstractBaseEAOTest<EntityEAOType, EntityType> {


    protected AbstractVersionedEAOTest(Class<? extends EntityEAOType> eaoBeanClass) {
        super(eaoBeanClass);
    }

    public AbstractVersionedEAOTest(Class<? extends EntityEAOType> eaoBeanClass, Class... embeddedClassesToDeploy) {
        super(eaoBeanClass, embeddedClassesToDeploy);
    }

    /**
     * @see VersionedEntity#createVersionInfo()
     * @param created created
     */
    @Override
    protected void assertBasePropertiesAfterCreation(EntityType created) {
        super.assertBasePropertiesAfterCreation(created);

        String createdBy = created.getCreator();
        assertNotNull(createdBy);
        // assertEquals(DEFAULT_CREATOR, createdBy);

        assertNull(created.getModifiedTime());
        assertNull(created.getModifier());
        assertEquals(0, created.getVersion().longValue());
    }

    /**
     * @see VersionedEntity#updateVersionInfo()
     * @param created created
     * @param mutated mutated
     */
    @Override
    protected void assertBasePropertiesAfterModification(EntityType created, EntityType mutated) {
        super.assertBasePropertiesAfterModification(created, mutated);

        assertEquals(created.getCreator(), mutated.getCreator());

        // make sure modified information is included
        assertNotNull(mutated.getModifiedTime());
        assertNotNull(mutated.getModifier());
        
        assertEquals(created.getVersion() + 1L, mutated.getVersion().longValue());
    }
}
