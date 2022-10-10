package com.xixun.joey.uart;

import com.xixun.joey.uart.IUartListener;

interface IUartService {
		boolean config(String devPathName, int baud);
		void read(String devPathName, IUartListener listener);
		boolean write(String devPathName, in byte[] data);
}