package vaks.com;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Patient 
{
	@JsonProperty("ad")
	private String firstName;
	@JsonProperty("soyad")
	private String lastName;
	private String dateOfBirth;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String toString() {
		return "Patient: [firstName:" + firstName + ", lastName:" + lastName + ", dateOfBirth:"+dateOfBirth+"]";
	}
}
