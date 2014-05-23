package com.salomon.perfman.model;

import com.salomon.perfman.util.Helper;

public class Phone {

	private String id;
	private String manufacturer;
	private boolean availability;
	private String model;
	private String osVersion;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public boolean isAvailability() {
		return this.availability;
	}

	public void setAvailability(boolean availability) {
		this.availability = availability;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getOsVersion() {
		return this.osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	/**
	 * 如：Xiaomi-MI-ONEPlus-4.1.2-1fb97386
	 */
	@Override
	public String toString() {
		String phoneInfo = Helper.combineStrings(this.getManufacturer(), "_",
				this.getModel(), "_", this.getOsVersion(), "_", this.getId());
		return phoneInfo.trim();
	}

	public Phone prasePhone(String phoneDescription) throws Exception {
		Phone phone = new Phone();
		String splits[] = phoneDescription.split("_");
		if (splits.length != 4) {
			throw new Exception("Error phone format");
		} else {
			phone.setManufacturer(splits[0]);
			phone.setModel(splits[1]);
			phone.setOsVersion(splits[2]);
			phone.setId(splits[3]);
		}
		return phone;
	}
}
