package com.xixun.joey.uart;

import android.os.Parcel;
import android.os.Parcelable;

public class BytesData implements Parcelable {
	private byte[] data;

	public BytesData() {
		data = new byte[1];
	}

	public BytesData(byte[] data) {
		if (null != data) {
			this.data = data;
		} else {
			this.data = new byte[1];
		}
	}

	public BytesData(Parcel source) {
		readFromParcel(source);
	}

	public byte[] getData() {
		return data;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(data.length);
		dest.writeByteArray(data);
	}

	public void readFromParcel(Parcel in) {
		data = new byte[in.readInt()];
		in.readByteArray(data);
	}

	public static final Creator<BytesData> CREATOR = new Creator<BytesData>() {

		@Override
		public BytesData createFromParcel(Parcel source) {
			return new BytesData(source);
		}

		@Override
		public BytesData[] newArray(int size) {
			return new BytesData[size];
		}
	};
}
