package rooppin.video.rental;

import java.util.*;
import javax.swing.*;

/**
 * Person class.
 * @author VADIM&ORI&MATAN
 *{@link JPanel}
 */
  @SuppressWarnings("serial")
class Person extends JPanel
{
	String Id;
	String Name;
	String Telephone;
	String Email;
	String City;
	String Street;
	int  House;
	int Entry;
	int Appartment;
	
	/**
	 * Default Constructor
	 */
	Person()
	{
		this.Id = "";
		this.Name = "guest";
		this.Telephone="";
		this.Email="";
		this.City = "";
		this.Street = "";
		this.House = 0;
		this.Entry = 0;
		this.Appartment = 0;
	}
	/**
	 * Parameter Constructor
	 * @param data {@link HashMap}
	 */
	
	Person(HashMap<String,String> data)
	{
		Id = data.get("Id");
		Name = data.get("Name");
		Telephone= data.get("Telephone");
		Email = data.get("Email");
		City = data.get("City");
		Street = data.get("Street");
		House = Integer.parseInt(data.get("House"));
		Entry = Integer.parseInt(data.get("Entry"));
		Appartment = Integer.parseInt(data.get("Appartment"));
	}
	
	/**
	 * Get id method
	 * @return String {@link String}
	 */
	String getId() {
		return Id;
	}
	
	/**
	 * Set id Method
	 * @param id {@link String}
	 */
	
	void setId(String id) {
		Id = id;
	}
	/**
	 * Get name method
	 * @return String {@link String}
	 */
	public String getName() {
		return Name;
	}
	/**
	 * Set name Method
	 * @param name {@link String}
	 */
	public void setName(String name) {
		Name = name;
	}
	/**
	 * Get email method
	 * @return String {@link String}
	 */
	String getEmail() {
		return Email;
	}
	/**
	 * Set email Method
	 * @param email {@link String}
	 */
	void setEmail(String email) {
		Email = email;
	}
	/**
	 * Get city method
	 * @return String {@link String}
	 */
	String getCity() {
		return City;
	}
	/**
	 * Set city Method
	 * @param city {@link String}
	 */
	void setCity(String city) {
		City = city;
	}
	/**
	 * Get street method
	 * @return String {@link String}
	 */
	String getStreet() {
		return Street;
	}
	/**
	 * Set street Method
	 * @param street {@link String}
	 */
	void setStreet(String street) {
		Street = street;
	}
	/**
	 * Get house method
	 * @return int
	 */
	int getHouse() {
		return House;
	}
	/**
	 * Set house Method
	 * @param house
	 */
	void setHouse(int house) {
		House = house;
	}
	/**
	 * Get entry method
	 * @return int
	 */
	int getEntry() {
		return Entry;
	}
	/**
	 * Set entry Method
	 * @param entry
	 */
	void setEntry(int entry) {
		Entry = entry;
	}
	/**
	 * Get appartment method
	 * @return int
	 */
	int getAppartment() {
		return Appartment;
	}
	/**
	 * Set appartment Method
	 * @param appartment
	 */
	void setAppartment(int appartment) {
		Appartment = appartment;
	}
	/**
	 * Get telephone method
	 * @return String {@link String}
	 */
	String getTelephone() {
		return Telephone;
	}
	/**
	 * Set telephone Method
	 * @param telephone {@link String}
	 */
	void setTelephone(String telephone) {
		Telephone = telephone;
	}
	
}