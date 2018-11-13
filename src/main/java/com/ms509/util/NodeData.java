package com.ms509.util;

public class NodeData
{
	public int nodetype;
	public String nodedata;
	public NodeData(int nodetype,String nodedata)
	{
		this.nodetype = nodetype;
		this.nodedata = nodedata;
	}
	public String toString() {
		return this.nodedata;
	}
	public class DataType
	{
		public final static int DATABASE = 1;
		public final static int TABLE = 2;
		public final static int COLUMN = 3;
	}
}
