package com.salomon.perfman.model;

public enum PerfType {
	Cpu((byte) 0), Mem((byte) 1), Traffic((byte) 2);
	private final byte value;

	public byte getValue() {
		return this.value;
	}

	PerfType(byte value) {
		this.value = value;
	}

	public static PerfType parse(byte value) {
		PerfType retValue = PerfType.Cpu;
		for (PerfType item : PerfType.values()) {
			if (value == item.getValue()) {
				retValue = item;
				break;
			}
		}

		return retValue;
	}
}
