package org.github.udaracamarasinghe.examples.simpledbeditorwebui;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.github.udaracamarasinghe.simpledbeditorwebui.apis.TableManager;
import org.github.udaracamarasinghe.simpledbeditorwebui.dto.KeyValue;
import org.github.udaracamarasinghe.simpledbeditorwebui.enums.ProcessFailType;
import org.github.udaracamarasinghe.simpledbeditorwebui.exceptions.EditableConfigException;

@Service
public class PersonsTableManagerImpl implements TableManager {

	private Map<String, Person> data;

	public PersonsTableManagerImpl() {
		data = new HashMap<>();

		data.put("Person1", new Person("Person1", 12));
		data.put("Person2", new Person("Person2", 34));
		data.put("Person3", new Person("Person3", 23));
		data.put("Person4", new Person("Person4", 67));
	}

	@Override
	public Map<String, Class<?>> tableDtoTypes() {
		HashMap<String, Class<?>> hashMap = new HashMap<>();
		hashMap.put("Persons", Person.class);
		return hashMap;
	}

	@Override
	public Object findRecords(String configName, String key) throws EditableConfigException {
		switch (configName) {

		case "Persons": {
			Person person = data.get(key);
			return person;
		}
		}

		return null;
	}

	@Override
	public Map<String, Object> findRecordsFor(String configName) throws EditableConfigException {
		Map<String, Object> map = new LinkedHashMap<>();

		switch (configName) {

		case "Persons": {
			for (Map.Entry<String, Person> entry : data.entrySet()) {
				map.put(entry.getKey(), entry.getValue());
			}

			break;
		}
		}

		return map;
	}

	@Override
	public Object newRecordEntry(String configName) throws EditableConfigException {
		switch (configName) {
		case "Persons": {
			return new Person();
		}
		}
		return null;
	}

	@Override
	public KeyValue saveRecordEntry(String configName, String key, Object record) throws EditableConfigException {
		try {
			switch (configName) {

			case "Persons": {
				Person p = (Person) record;
				data.put(p.getName(), (Person) record);

				return new KeyValue(p.getName(), p);
			}
			}
		} catch (Exception ex) {
			throw new EditableConfigException("Failed to save config, due to " + ex.getMessage(), ex);
		}

		throw new EditableConfigException("New maching Config entry type found.");
	}

	@Override
	public KeyValue updateRecordEntry(String configName, String key, Object record) throws EditableConfigException {
		try {
			switch (configName) {
			case "Persons": {
				if (record instanceof Person) {
					Person rr = (Person) record;
					Person person = data.get(key);

					person.setName(rr.getName());
					person.setAge(rr.getAge());

					data.put(key, person);

					return new KeyValue(key, person);
				}
			}
			}

		} catch (

		Exception ex) {
			throw new EditableConfigException("Failed to save config, due to " + ex.getMessage(), ex);
		}

		throw new EditableConfigException("New maching Config entry type found.");
	}

	@Override
	public void removeRecordEntry(String configName, String key) throws EditableConfigException {
		try {
			switch (configName) {
			case "Persons": {
				data.remove(key);

				return;
			}
			}
		} catch (Exception ex) {
			throw new EditableConfigException("Failed to remove config, due to " + ex.getMessage(), ex);
		}

		throw new EditableConfigException("New maching Config entry type found.");
	}

	@Override
	public Object emptyRecordEntry(String configName) throws EditableConfigException {
		switch (configName) {
		case "Loan-Profile-Config": {
			throw new EditableConfigException(ProcessFailType.BAD_REQUEST,
					"New entries not allowed in Loan-Profile-Config.");
		}
		}

		return null;
	}

}
