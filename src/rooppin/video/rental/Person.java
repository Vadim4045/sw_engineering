package rooppin.video.rental;

import java.util.*;
import javax.swing.*;

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
	
	
	String getId() {
		return Id;
	}
	
	void setId(String id) {
		Id = id;
	}
	
	public String getName() {
		return Name;
	}
	
	public void setName(String name) {
		Name = name;
	}
	
	String getEmail() {
		return Email;
	}

	void setEmail(String email) {
		Email = email;
	}
	
	String getCity() {
		return City;
	}
	
	void setCity(String city) {
		City = city;
	}
	
	String getStreet() {
		return Street;
	}
	
	void setStreet(String street) {
		Street = street;
	}
	
	int getHouse() {
		return House;
	}
	
	void setHouse(int house) {
		House = house;
	}
	
	int getEntry() {
		return Entry;
	}
	
	void setEntry(int entry) {
		Entry = entry;
	}
	
	int getAppartment() {
		return Appartment;
	}
	
	void setAppartment(int appartment) {
		Appartment = appartment;
	}

	String getTelephone() {
		return Telephone;
	}

	void setTelephone(String telephone) {
		Telephone = telephone;
	}
	
}