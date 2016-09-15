package com.akuacom.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * <tt> MasterDetailFactory </tt> can create a Pojo, called Master Object, which contains multiple detail objects.   
 * <p> A typical example Order and items. 
 * <PRE>
 * //mapped to table order
 * public class Order {
 *     private String orderId; //mapped to primary key
 *     private String customer;
 *     private List<Item> items;
 * 
 *    //setters and getters 
 *    ......
 *  }
 *  
 *  //mapped to table order_item
 *  public class Item {
 *     private Order order; //mapped to foreign key
 *     private String productName; 
 *  	
 *     //setters and getters 
 *     ...... 
 *  }
 *  
 *  //SQL to get all orders and items from database
 *  String sql = "select a.orderId, a.customer, b. productName"
 *  			+ " from order a, order_Item b where b.orderId = a.orderId";
 *  MasterDetailFactory<Order,Item> factory = new MasterDetailFactory<Order,Item>(
 *		          new ColumnAsFeatureFactory<Order>(Order.class),new ColumnAsFeatureFactory<Item>(Item.class),"orderId"){
 *	  private static final long serialVersionUID = 1L;
 *			
 *	  public void buildUp(Order order, Item item){
 *	    if(order.getItems()==null){
 *	      order.setItems(new ArrayList<Item>());
 *	    }
 *	    order.getItems().add(item);
 *  	item.setOrder(order);
 *	  }
 *  };
 *  
 *  SQLExecutor sqlcommander = lookup(...);
 *  List<order> orders =sqlcommander.doNativeQuery(getConnection(),sql, new ListConverter<Order>(factory);
 *  
 *  for(Order order: orders){
 *      System.out.println(order.getOrderId());
 *      for(Item item: order.getItems()){
 *  	    System.out.println(item.getProductName());
 *      }
 *  }
 *  
 * </PRE>
 * 
 */

public abstract class MasterDetailFactory<T,E> implements BeanFactory<T> {
	
	private static final long serialVersionUID = 1744823661294397789L;

	private BeanFactory<T> masterFactory;
	
	private BeanFactory<E> detailFactory;
	
	private String[] masterKeycolumn;
	
	private Map<Long,T> masterCache;
	
	private long lastKey = -1;
	
	private boolean masterInOrder;
	
	private T lastMaster;
	
	/**
	 * @param masterFactory   BeanFactory to create master Object 
	 * @param detailFactory   BeanFactory to create detail Object 
	 * @param masterKeycolumn columns which value can be used to identify a row for master object 
	 */
	public MasterDetailFactory(BeanFactory<T> masterFactory, BeanFactory<E> detailFactory){
		this(masterFactory,detailFactory,false);
	}
	
	/**
	 * @param masterFactory   BeanFactory to create master Object 
	 * @param detailFactory   BeanFactory to create detail Object 
	 * @param masterKeycolumn columns which value can be used to identify a row for master object 
	 */
	public MasterDetailFactory(BeanFactory<T> masterFactory, BeanFactory<E> detailFactory, boolean masterInOrder){
		this.masterFactory = masterFactory;
		this.detailFactory = detailFactory;
		this.masterKeycolumn= masterFactory.getKeyColumns();
		if(this.masterKeycolumn==null ||masterKeycolumn.length==0 ){
			throw new IllegalArgumentException("master factory key column(s) must be specified");
		}
		this.masterInOrder = masterInOrder;
		if(!masterInOrder)
			masterCache = new HashMap<Long, T>();
	}
	
	public T createInstance(ResultSet rs) throws SQLException{
		long hashCode = 0;
		boolean newInstance = false;
		HashCodeBuilder hb = new HashCodeBuilder();
		for(String column: masterKeycolumn){
			Object key =rs.getObject(column);
			hb.append(key);
		}
		hashCode = hb.toHashCode();
		
		T master = null;
		if(!masterInOrder){
			master =masterCache.get(hashCode);
		}else{
			if(lastKey == hashCode){
				master = lastMaster;
			}
			lastKey = hashCode;
		}
		
		
		E detail =detailFactory.createInstance(rs);
		if(master ==null){
			master =masterFactory.createInstance(rs);
			if(!masterInOrder){
				masterCache.put(hashCode, master);
			}else{
				lastMaster = master;
			}
			newInstance= true;
		}
		buildUp(master,detail);
		
		if(newInstance)
			return master;
		else
			return null;
	}
	
	/**
	 * How to build up connection between master and detail obejct,
	 * <p> For example add detail object to a list in master object or put detail object in a map 
	 * @param master
	 * @param detail
	 */
	abstract protected void buildUp(T master, E detail);
	
	
	public String[] getKeyColumns() {
		return this.masterKeycolumn;
	}
}
