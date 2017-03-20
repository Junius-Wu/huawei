package com.lab603.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.lab603.module.CostNode;
import com.lab603.module.Net;
import com.lab603.module.Node;
import com.lab603.module.Tran;

public class TransStringToModule {
	public static Net fromStrings(String[] input) {
		int index = 0;
		
		// 第0行 ：网络节点數，链路數，消费节点數
		String[] countString = input[index++].split(" ");
		int nodeCount = Integer.valueOf(countString[0]);
		int tranCount = Integer.valueOf(countString[1]);
		int costNodeCount = Integer.valueOf(countString[2]);
		
		boolean[][] connected = new boolean[nodeCount][nodeCount];
		
		// 第2行：服务器部署成本
		index++;//跳过空行
		int serverCost = Integer.valueOf(input[index++]);
		index++;//跳过空行
		
		// 第四行   所有链路数据 ：鏈路起點 終止點 總帶寬 單位費用
		ArrayList<Tran> trans = new ArrayList<>(tranCount);
		for(; index < 4 + tranCount; index ++) {
			String[] tranDataString = input[index].split(" ");
			int fromNodeID = Integer.valueOf(tranDataString[0]);
			int toNodeID = Integer.valueOf(tranDataString[1]);
			int maxValue = Integer.valueOf(tranDataString[2]);
			int costValue = Integer.valueOf(tranDataString[3]);
			connected[fromNodeID][toNodeID] = true;
			Tran tran = new Tran(fromNodeID, toNodeID, maxValue, costValue);
			trans.add(tran);
		}
		
		index++;// 跳过空行
		// 第n行 读取消费节点数据：id 连接的node 需求
		ArrayList<CostNode> costNodes = new ArrayList<>(costNodeCount);
		for(; index < 5 + tranCount + costNodeCount; index++) {
			String[] costNodeDataString = input[index].split(" ");
			int id = Integer.valueOf(costNodeDataString[0]);
			int linkedNodeId = Integer.valueOf(costNodeDataString[1]);
			int requestValue = Integer.valueOf(costNodeDataString[2]);
			CostNode costNode = new CostNode(id, linkedNodeId, requestValue);
			costNodes.add(costNode);
		}
		// 添加从0 到nodeCount 的普通节点
		ArrayList<Node> nodes = new ArrayList<>(nodeCount);
		for(int i = 0; i < nodeCount; i++) {
			Node node = new Node(i, false);
			nodes.add(node);
		}
		
		Net net = new Net(connected, nodes, trans, costNodes, serverCost);
		return net; 
		
	}
	
	public static void main(String args[]) throws IOException {
		FileReader fr = new FileReader("case0.txt");
		 BufferedReader br = new BufferedReader(fr);
        String s;
        StringBuilder sb = new StringBuilder();// 作为写入String
        ArrayList<String> strings = new ArrayList<>();
        while ((s = br.readLine()) != null) {// 读取一行字符
            System.out.println(s);
            strings.add(s);
        }

	    br.close();
	    String[] input = new String[strings.size()];
	    for(int i = 0; i < input.length; i++) {
	    	input[i] = strings.get(i);
	    }
	    
		fromStrings(input);
	}
}
