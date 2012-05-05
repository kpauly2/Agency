package abce.io.simple.ecj;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;



public class SimpleAgencyConfig extends Properties {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	public double				firm_initial_price;
	public double				firm_endowment;
	public int					number_of_customers;
	public int					persons_per_consumer_agent;
	public double				willingness_to_pay_high;
	public double				willingness_to_pay_low;
	public double				cost_constant;
	public double				price_constant;
	public int					steps_to_run;



	public SimpleAgencyConfig(String file_path) throws FileNotFoundException, IOException {
		FileInputStream fin = null;
		fin = new FileInputStream(file_path);
		load(fin);
		fin.close();
		register();
	}



	protected void register() {
		firm_initial_price = D("firm_initial_price");
		firm_endowment = D("firm_endowment");
		number_of_customers = I("number_of_customers");
		persons_per_consumer_agent = I("persons_per_consumer_agent");
		willingness_to_pay_high = D("willingness_to_pay_high");
		willingness_to_pay_low = D("willingness_to_pay_low");
		cost_constant = D("cost_constant");
//		price_constant = D("price_constant");
		steps_to_run = I("steps_to_run");
	}



	protected Double D(String key) {
		return Double.valueOf(getProperty(key));
	}



	protected Integer I(String key) {
		return Integer.valueOf(getProperty(key));
	}



	protected String S(String key) {
		return getProperty(key);
	}
}
