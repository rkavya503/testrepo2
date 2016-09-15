<deployment xmlns="urn:jboss:bean-deployer:2.0">
  <!-- This bean is an example of a clustered singleton -->
  <bean name="HATimerSingleton" class="com.akuacom.pss2.singleton.HATimerSingleton">
    <!--annotation>@org.jboss.aop.microcontainer.aspects.jmx.JMX(...)</annotation-->
  </bean>

  <bean name="HATimerSingletonController" class="org.jboss.ha.singleton.HASingletonController">
    <!--annotation>@org.jboss.aop.microcontainer.aspects.jmx.JMX(...)</annotation-->
    <property name="HAPartition"><inject bean="HAPartition"/></property>
    <property name="target"><inject bean="HATimerSingleton"/></property>
    <property name="targetStartMethod">startSingleton</property>
    <property name="targetStopMethod">stopSingleton</property>
  </bean>
</deployment>
