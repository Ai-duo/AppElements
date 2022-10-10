package com.xixun.joey.uart;

import com.xixun.joey.uart.BytesData;

interface IUartListener {
		/**
		 * data: 返回接收到的串口数据
		 */
		void onReceive(inout BytesData data);
}