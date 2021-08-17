package vaks.com;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import vaks.com.dto.Patient;
import vaks.com.dto.UserPost;

public class JacksonDemo {

	public static void main(String args[]) throws Exception {
		// jsonReadUrl();
		// jsonReadAsClass();
		// jsonReadModify();
		jsonStreamRead();
		// jsonStreamWrite();
		// jsonRead();
		// jsonWrite();
	}

	public static void jsonReadUrl() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			UserPost[] values = mapper.readValue(new URL("http://jsonplaceholder.typicode.com/posts"),
					UserPost[].class);
			// or:
			// List<UserPost> userPostList = mapper.readValue(new
			// URL("http://jsonplaceholder.typicode.com/posts"), new
			// TypeReference<List<UserPost>>() {});
			Arrays.asList(values).forEach(s -> {
				try {
					System.out.println(mapper.writeValueAsString(s));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void jsonReadAsClass() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setSerializationInclusion(Include.NON_NULL);

		try {
			Patient patient = objectMapper.readValue("{\"ad\" : \"John\",  \"soyad\" : \"Oliver\", \"salary\" : 1500}",
					Patient.class);
			System.out.println(patient.getFirstName());
			System.out.println(patient.getDateOfBirth());
			System.out.println(patient.toString());
			objectMapper.writeValue(System.out, patient);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void jsonReadModify() {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			UserPost[] readValue = objectMapper.readValue(new URL("http://jsonplaceholder.typicode.com/posts"),
					UserPost[].class);
			objectMapper.writeValue(System.out, readValue.length);
			System.out.println(objectMapper.writeValueAsString(readValue[0]));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void jsonStreamRead() {
		JsonFactory factory = new JsonFactory();
		try {
			JsonParser parser = factory
					.createParser(new File(JacksonDemo.class.getClassLoader().getResource("patient.json").getPath()));
			while (parser.nextToken() != null) {
				JsonToken token = parser.getCurrentToken();
				System.out.println(token);
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void jsonStreamWrite() {
		try {
			JsonFactory factory = new JsonFactory();
			JsonGenerator generator = factory.createGenerator(System.out);
			generator.setPrettyPrinter(new DefaultPrettyPrinter());
			generator.writeStartObject();
			generator.writeStringField("firstName", "John");
			generator.writeStringField("lastName", "Oliver");
			generator.writeFieldName("visitedCities");
			generator.writeStartArray();
			generator.writeString("Ankara");
			generator.writeString("Antalya");
			generator.writeEndArray();
			generator.writeEndObject();
			generator.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void jsonRead() {
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		objectMapper.setDateFormat(dateFormat);
		try {
			Patient patient = objectMapper.readValue(
					new File(JacksonDemo.class.getClassLoader().getResource("patient.json").getPath()), Patient.class);
			System.out.println(patient.getFirstName());
			System.out.println(patient.getDateOfBirth());
			System.out.println(patient.toString());
			System.out.println(objectMapper.writeValueAsString(dateFormat.parse(patient.getDateOfBirth())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void jsonWrite() {
		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		Patient patient = new Patient();
		patient.setFirstName("Johnson");
		patient.setLastName("Oliver");

		try {
			System.out.println(objectMapper.writeValueAsString(patient));
			System.out.println(JacksonDemo.class.getClassLoader().getResource("patient.json").getPath());
			objectMapper.writeValue(new File(JacksonDemo.class.getClassLoader().getResource("patient.json").getPath()),
					patient);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
