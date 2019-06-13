package com.hlzn.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JLTest implements Callable<Integer> {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        String message = "PENsaW5pY2FsRG9jdW1lbnQgeG1sbnM9InVybjpzYy13c3Q6djIiIHhtbG5zOnhzaT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEtaW5zdGFuY2UiICB4c2k6c2NoZW1hTG9jYXRpb249InVybjpzYy13c3Q6djIgU0QueHNkIj4NCiAgICA8aWQgcm9vdD0iMi4xNi4xNTYuMTAwMTEuMS4xIiBleHRlbnNpb249ImVkMjZkYWYwLTVmNTItNDJmYi04Njg5LWIxNTMzNzE1ZDFhMSIvPg0KICAgIDxjb2RlIGNvZGU9IkMwMDA0IiBkaXNwbGF5TmFtZT0i6KW/6I2v5aSE5pa5IiBjb2RlU3lzdGVtPSJXUy9UIDQ0NS0yMDE0IiBjb2RlU3lzdGVtTmFtZT0i55S15a2Q55eF5Y6G5Z+65pys5pWw5o2u6ZuGIi8+DQogICAgPGVmZmVjdGl2ZVRpbWUgdmFsdWU9IjIwMTQwNjE5MDkxMzMyIi8+DQogICAgPHBhdGllbnQ+DQogICAgICAgIDxzb3VyY2VpZCB2YWx1ZT0iQkYzMkRTRUY5OENFQUdIRSIvPg0KICAgICAgICA8aWQgcm9vdD0iMi4xNi4xNTYuMTAwMTEuMS4zNSIgZXh0ZW5zaW9uPSI1MTA2MjMxMDEyMDIwMTAwMTAwMSIvPjwhLS0gIOaWsOWGnOWQiOWPtyAgT0lEICDmmoLml7bpooTnlZktLT4NCiAgICAgICAgPGlkIHJvb3Q9IjIuMTYuMTU2LjEwMDExLjEuMyIgZXh0ZW5zaW9uPSI1MTM3MjMxOTg1MTIwMTU2NzEiLz48IS0tIOi6q+S7veivgeWPtyAtLT4NCiAgICAgICAgPGlkIHJvb3Q9IjIuMTYuMTU2LjEwMDExLjEuMTEiIGV4dGVuc2lvbj0iMDEiLz48IS0tIOmXqCjmgKUp6K+K5Y+35qCH6K+GIC0tPg0KICAgICAgICA8aWQgcm9vdD0iMi4xNi4xNTYuMTAwMTEuMS4yMCIgZXh0ZW5zaW9uPSIxMjMiLz48IS0t5aSE5pa557yW5Y+35qCH6K+GIC0tPg0KICAgICAgICA8aXRlbSB4c2k6dHlwZT0iU1QiIG5pZD0iSERTRDAwLjA0LjAxNCIgbmFtZT0i5oKj6ICF5aeT5ZCNIiB2YWx1ZT0i5byg5LiJIi8+DQogICAgICAgIDxpdGVtIHhzaTp0eXBlPSJDRCIgbmlkPSJIRFNEMDAuMDQuMDIwIiBuYW1lPSLmgKfliKvku6PnoIEiIHZhbHVlPSIyIiBjb2RlU3lzdGVtPSIyLjE2LjE1Ni4xMDAxMS4yLjMuMy40Ii8+DQogICAgICAgIDxpdGVtIHhzaTp0eXBlPSJQUSIgbmlkPSJIRFNEMDAuMDQuMDE3IiBuYW1lPSLlubTpvoQiIHZhbHVlPSIyMyIgIHVuaXQ9IuWygSIvPg0KICAgICAgICA8aXRlbSB4c2k6dHlwZT0iUFEiIG5pZD0iSERTRDAwLjA0LjAxOCIgbmFtZT0i5bm06b6EIiB2YWx1ZT0iMiIgIHVuaXQ9IuaciCIvPg0KICAgICAgICA8aXRlbSB4c2k6dHlwZT0iU1QiIG5pZD0iSERTRDAwLjA0LjAwNSIgbmFtZT0i5aSE5pa55byA56uL56eR5a6k5ZCN56ewIiB2YWx1ZT0i6ICz6by75ZaJIi8+DQogICAgICAgIDxpdGVtIHhzaTp0eXBlPSJDRCIgbmlkPSJIRFNEMDAuMDQuMDI5IiBuYW1lPSLljLvnlpfmnLrmnoTnu4Tnu4fmnLrmnoTku6PnoIEiIHZhbHVlPSJsbGxsIiBjb2RlU3lzdGVtPSIyLjE2LjE1Ni4xMDAxMS4yLjMuNC4xIiAvPg0KICAgIDwvcGF0aWVudD4NCiAgICA8YXV0aG9yIGNvZGU9IjE0MTUxMDAwMDAwMTUwMiIgbmFtZT0i5b6Q5b6BIj4NCiAgICAgICAgPHRpbWUgdmFsdWU9IjE5ODAxMDMwIi8+ICA8IS0tIOWkhOaWueW8gOeri+aXpeacnyAtLT4NCiAgICA8L2F1dGhvcj4NCiAgICA8Y3VzdG9kaWFuPg0KICAgICAgICA8aWQgcm9vdD0iMi4xNi4xNTYuMTAwMTEuMS42IiBleHRlbnNpb249IjAwODM4MDQzMSIvPiAgPCEtLSBAZXh0ZW5zaW9u77ya5paH5qGj5L+d566h55qE5Yy755aX5py65p6E5qCH6K+G57yW56CBIC0tPg0KICAgICAgICA8bmFtZT7ljavnlJ/lsYDlgaXlurfmoaPmoYjnrqHnkIbkuK3lv4M8L25hbWU+PCEtLSDmlofmoaPkv53nrqHnmoTljLvnlpfmnLrmnoTlkI3np7AgLS0+DQogICAgICAgIDxhZGRyPuW6kOWxseWNl+i3r+S6jOautTI5OeWPtzwvYWRkcj48IS0tIOaWh+aho+S/neeuoeeahOWMu+eWl+acuuaehOWcsOWdgCAtLT4NCiAgICA8L2N1c3RvZGlhbj4NCiAgICA8bGVnYWw+DQogICAgICAgIDx0aW1lIHZhbHVlPSIyMDEwMDEwMSIvPg0KICAgICAgICA8aWQgcm9vdD0iMi4xNi4xNTYuMTAwMTEuMS40IiBleHRlbnNpb249IjAwMTIiLz4NCiAgICAgICAgPG5hbWU+5p2O5bCP5LiJPC9uYW1lPjwhLS0g5aSE5pa55a6h5qC46I2v5YmC5biI562+5ZCNIC0tPg0KICAgIDwvbGVnYWw+DQogICAgPGF1dGhlbnRpY2F0b3IgdXNlPSJQUCI+DQogICAgICAgIDx0aW1lIHZhbHVlPSIyMDEwMDEwMSIvPg0KICAgICAgICA8aWQgcm9vdD0iMi4xNi4xNTYuMTAwMTEuMS40IiBleHRlbnNpb249IjAwMTMiLz4NCiAgICAgICAgPG5hbWU+546L5bCP5LqMPC9uYW1lPg0KICAgIDwvYXV0aGVudGljYXRvcj4NCiAgICA8YXV0aGVudGljYXRvciB1c2U9IkRDIj4NCiAgICAgICAgPHRpbWUgdmFsdWU9IjIwMTAwMTAxIi8+DQogICAgICAgIDxpZCByb290PSIyLjE2LjE1Ni4xMDAxMS4xLjQiIGV4dGVuc2lvbj0iMDAxNCIvPg0KICAgICAgICA8bmFtZT7kuIrlsI/nk5w8L25hbWU+DQogICAgPC9hdXRoZW50aWNhdG9yPg0KICAgIDxhdXRoZW50aWNhdG9yIHVzZT0iTUQiPg0KICAgICAgICA8dGltZSB2YWx1ZT0iMjAxMDAxMDEiLz4NCiAgICAgICAgPGlkIHJvb3Q9IjIuMTYuMTU2LjEwMDExLjEuNCIgZXh0ZW5zaW9uPSIwMDE1Ii8+DQogICAgICAgIDxuYW1lPuWFg+S4nOS4nDwvbmFtZT4NCiAgICA8L2F1dGhlbnRpY2F0b3I+DQogICAgPHN0cnVjdHVyZWRCb2R5Pg0KICAgICAgICA8c2VjdGlvbiBjb2RlPSIyOTU0OC01IiBkaXNwbGF5TmFtZT0iRGlhZ25vc2lzIj4NCiAgICAgICAgICAgIDxsaXN0IG5hbWU9IkNETCI+DQogICAgICAgICAgICAgICAgPGVudHJ5Pg0KICAgICAgICAgICAgICAgICAgICA8aXRlbSB4c2k6dHlwZT0iQ0QiIG5pZD0iSERTRDAwLjA0LjAxNSIgbmFtZT0i55a+55eF6K+K5pat57yW56CBIiB2YWx1ZT0iSzMyLjAxIiBjb2RlU3lzdGVtPSIyLjE2LjE1Ni4xMDAxMS4yLjMuMy4xMS4zICIvPjwhLS0gMi4xNi4xNTYuMTAwMTEuMi4zLjMuMTEuMyAgSUNELTEwIC0tPg0KICAgICAgICAgICAgICAgIDwvZW50cnk+DQogICAgICAgICAgICAgICAgPGVudHJ5Pg0KICAgICAgICAgICAgICAgICAgICA8aXRlbSB4c2k6dHlwZT0iQ0QiIG5pZD0iSERTRDAwLjA0LjAxNSIgbmFtZT0i55a+55eF6K+K5pat57yW56CBIiB2YWx1ZT0iSzMyLjAyIiBjb2RlU3lzdGVtPSIyLjE2LjE1Ni4xMDAxMS4yLjMuMy4xMS4zICIvPjwhLS0gMi4xNi4xNTYuMTAwMTEuMi4zLjMuMTEuMyAgSUNELTEwIC0tPg0KICAgICAgICAgICAgICAgIDwvZW50cnk+DQogICAgICAgICAgICA8L2xpc3Q+DQogICAgICAgIDwvc2VjdGlvbj4NCiAgICAgICAgPHNlY3Rpb24gY29kZT0iMTAxNjAtMCIgZGlzcGxheU5hbWU9IkhJU1RPUlkgT0YgTUVESUNBVElPTiBVU0UiPg0KICAgICAgICAgICAgPGl0ZW0geHNpOnR5cGU9IlNUIiBuaWQ9IkhEU0QwMC4wNC4wMTIiIG5hbWU9IuWkhOaWueiNr+WTgee7hOWPtyIgdmFsdWU9IjMzMyIvPjwhLS0g5Y+q6IO95pivTuWeiyAtLT4NCiAgICAgICAgICAgIDxpdGVtIHhzaTp0eXBlPSJQUSIgbmlkPSJIRFNEMDAuMDQuMDEzIiBuYW1lPSLlpITmlrnmnInmlYjlpKnmlbAiIHZhbHVlPSIzIiB1bml0PSLlpKkiLz4NCiAgICAgICAgICAgIDxpdGVtIHhzaTp0eXBlPSJTVCIgbmlkPSJIRFNEMDAuMDQuMDAxIiBuYW1lPSLlpITmlrnlpIfms6jkv6Hmga8iIHZhbHVlPSLopoHlpIfms6jku4DkuYgiLz4NCiAgICAgICAgICAgIDxsaXN0IG5hbWU9IkhNVSI+DQogICAgICAgICAgICAgICAgPGVudHJ5Pg0KICAgICAgICAgICAgICAgICAgICA8aXRlbSB4c2k6dHlwZT0iU1QiIG5pZD0iSERTRDAwLjA0LjAyMyIgbmFtZT0i6I2v54mp5ZCN56ewIiB2YWx1ZT0i6Zi/6I6r6KW/5p6XIi8+DQogICAgICAgICAgICAgICAgICAgIDxpdGVtIHhzaTp0eXBlPSJTVCIgbmlkPSJIRFNEMDAuMDQuMDIxIiBuYW1lPSLoja/nianop4TmoLwiIHZhbHVlPSIzMG1nIi8+DQogICAgICAgICAgICAgICAgICAgIDxpdGVtIHhzaTp0eXBlPSJDRCIgbmlkPSJIRFNEMDAuMDQuMDIyIiBuYW1lPSLoja/nianliYLlnovku6PnoIEiIHZhbHVlPSIwMSIgY29kZVN5c3RlbT0iMi4xNi4xNTYuMTAwMTEuMi4zLjEuMjExIi8+DQogICAgICAgICAgICAgICAgICAgIDxpdGVtIHhzaTp0eXBlPSJQUSIgbmlkPSJIRFNEMDAuMDQuMDI0IiBuYW1lPSLoja/niankvb/nlKjmrKHliYLph48iIHZhbHVlPSI1IiB1bml0PSJtZyIvPg0KICAgICAgICAgICAgICAgICAgICA8aXRlbSB4c2k6dHlwZT0iU1QiIG5pZD0iSERTRDAwLjA0LjAyNSIgbmFtZT0i6I2v54mp5L2/55So5YmC6YeP5Y2V5L2NIiB2YWx1ZT0ibWciLz4NCiAgICAgICAgICAgICAgICAgICAgPGl0ZW0geHNpOnR5cGU9IkNEIiBuaWQ9IkhEU0QwMC4wNC4wMjYiIG5hbWU9IuiNr+eJqeS9v+eUqOmikeasoeS7o+eggSIgdmFsdWU9IjMiIGNvZGVTeXN0ZW09IjIuMTYuMTU2LjEwMDExLjIuMy4xLjI2NyIvPg0KICAgICAgICAgICAgICAgICAgICA8aXRlbSB4c2k6dHlwZT0iUFEiIG5pZD0iSERTRDAwLjA0LjAyOCIgbmFtZT0i6I2v54mp5L2/55So5oC75YmC6YePIiB2YWx1ZT0iNjAiIHVuaXQ9Im1nIi8+DQogICAgICAgICAgICAgICAgICAgIDxpdGVtIHhzaTp0eXBlPSJDRCIgbmlkPSJIRFNEMDAuMDQuMDI3IiBuYW1lPSLnlKjoja/pgJTlvoTku6PnoIEiIHZhbHVlPSIxIiBjb2RlU3lzdGVtPSIyLjE2LjE1Ni4xMDAxMS4yLjMuMS4xNTgiLz48IS0tIDIuMTYuMTU2LjEwMDExLjIuMy4xLjE1OCAtLT4NCiAgICAgICAgICAgICAgICA8L2VudHJ5Pg0KICAgICAgICAgICAgICAgIDxlbnRyeT4NCiAgICAgICAgICAgICAgICAgICAgPGl0ZW0geHNpOnR5cGU9IlNUIiBuaWQ9IkhEU0QwMC4wNC4wMjMiIG5hbWU9IuiNr+eJqeWQjeensCIgdmFsdWU9Ijk5OeaEn+WGkueBtSIvPg0KICAgICAgICAgICAgICAgICAgICA8aXRlbSB4c2k6dHlwZT0iU1QiIG5pZD0iSERTRDAwLjA0LjAyMSIgbmFtZT0i6I2v54mp6KeE5qC8IiB2YWx1ZT0iNDhtZyIvPg0KICAgICAgICAgICAgICAgICAgICA8aXRlbSB4c2k6dHlwZT0iQ0QiIG5pZD0iSERTRDAwLjA0LjAyMiIgbmFtZT0i6I2v54mp5YmC5Z6L5Luj56CBIiB2YWx1ZT0iMDMiIGNvZGVTeXN0ZW09IjIuMTYuMTU2LjEwMDExLjIuMy4xLjIxMSIvPg0KICAgICAgICAgICAgICAgICAgICA8aXRlbSB4c2k6dHlwZT0iUFEiIG5pZD0iSERTRDAwLjA0LjAyNCIgbmFtZT0i6I2v54mp5L2/55So5qyh5YmC6YePIiB2YWx1ZT0iOCIgdW5pdD0ibWciLz4NCiAgICAgICAgICAgICAgICAgICAgPGl0ZW0geHNpOnR5cGU9IlNUIiBuaWQ9IkhEU0QwMC4wNC4wMjUiIG5hbWU9IuiNr+eJqeS9v+eUqOWJgumHj+WNleS9jSIgdmFsdWU9Im1nIi8+DQogICAgICAgICAgICAgICAgICAgIDxpdGVtIHhzaTp0eXBlPSJDRCIgbmlkPSJIRFNEMDAuMDQuMDI2IiBuYW1lPSLoja/niankvb/nlKjpopHmrKHku6PnoIEiIHZhbHVlPSIzIiBjb2RlU3lzdGVtPSIyLjE2LjE1Ni4xMDAxMS4yLjMuMS4yNjciLz4NCiAgICAgICAgICAgICAgICAgICAgPGl0ZW0geHNpOnR5cGU9IlBRIiBuaWQ9IkhEU0QwMC4wNC4wMjgiIG5hbWU9IuiNr+eJqeS9v+eUqOaAu+WJgumHjyIgdmFsdWU9Ijk2IiB1bml0PSJtZyIvPg0KICAgICAgICAgICAgICAgICAgICA8aXRlbSB4c2k6dHlwZT0iQ0QiIG5pZD0iSERTRDAwLjA0LjAyNyIgbmFtZT0i55So6I2v6YCU5b6E5Luj56CBIiB2YWx1ZT0iMSIgY29kZVN5c3RlbT0iMi4xNi4xNTYuMTAwMTEuMi4zLjEuMTU4Ii8+PCEtLSAyLjE2LjE1Ni4xMDAxMS4yLjMuMS4xNTggLS0+DQogICAgICAgICAgICAgICAgPC9lbnRyeT4NCiAgICAgICAgICAgIDwvbGlzdD4gDQogICAgICAgICAgICA8bGlzdCBuYW1lPSJHVEciPg0KICAgICAgICAgICAgICAgIDxlbnRyeT4NCiAgICAgICAgICAgICAgICAgICAgPGl0ZW0geHNpOnR5cGU9IlNUIiBuaWQ9IkhEU0QwMC4wNC4wMjMiIG5hbWU9IuiNr+eJqeWQjeensCIgdmFsdWU9IuWSs+WXveezlua1hiIvPg0KICAgICAgICAgICAgICAgICAgICA8aXRlbSB4c2k6dHlwZT0iU1QiIG5pZD0iSERTRDAwLjA0LjAyMSIgbmFtZT0i6I2v54mp6KeE5qC8IiB2YWx1ZT0iNTBtbCIvPg0KICAgICAgICAgICAgICAgICAgICA8aXRlbSB4c2k6dHlwZT0iQ0QiIG5pZD0iSERTRDAwLjA0LjAyMiIgbmFtZT0i6I2v54mp5YmC5Z6L5Luj56CBIiB2YWx1ZT0iMDIiIGNvZGVTeXN0ZW09IjIuMTYuMTU2LjEwMDExLjIuMy4xLjIxMSIvPg0KICAgICAgICAgICAgICAgICAgICA8aXRlbSB4c2k6dHlwZT0iUFEiIG5pZD0iSERTRDAwLjA0LjAyNCIgbmFtZT0i6I2v54mp5L2/55So5qyh5YmC6YePIiB2YWx1ZT0iMTAiIHVuaXQ9Im1sIi8+DQogICAgICAgICAgICAgICAgICAgIDxpdGVtIHhzaTp0eXBlPSJTVCIgbmlkPSJIRFNEMDAuMDQuMDI1IiBuYW1lPSLoja/niankvb/nlKjliYLph4/ljZXkvY0iIHZhbHVlPSJtZyIvPg0KICAgICAgICAgICAgICAgICAgICA8aXRlbSB4c2k6dHlwZT0iQ0QiIG5pZD0iSERTRDAwLjA0LjAyNiIgbmFtZT0i6I2v54mp5L2/55So6aKR5qyh5Luj56CBIiB2YWx1ZT0iMyIgY29kZVN5c3RlbT0iMi4xNi4xNTYuMTAwMTEuMi4zLjEuMjY3Ii8+DQogICAgICAgICAgICAgICAgICAgIDxpdGVtIHhzaTp0eXBlPSJQUSIgbmlkPSJIRFNEMDAuMDQuMDI4IiBuYW1lPSLoja/niankvb/nlKjmgLvliYLph48iIHZhbHVlPSIxMDAiIHVuaXQ9Im1sIi8+DQogICAgICAgICAgICAgICAgICAgIDxpdGVtIHhzaTp0eXBlPSJDRCIgbmlkPSJIRFNEMDAuMDQuMDI3IiBuYW1lPSLnlKjoja/pgJTlvoTku6PnoIEiIHZhbHVlPSIxIiBjb2RlU3lzdGVtPSIyLjE2LjE1Ni4xMDAxMS4yLjMuMS4xNTgiLz48IS0tIDIuMTYuMTU2LjEwMDExLjIuMy4xLjE1OCAtLT4NCiAgICAgICAgICAgICAgICA8L2VudHJ5Pg0KICAgICAgICAgICAgPC9saXN0Pg0KICAgICAgICA8L3NlY3Rpb24+DQogICAgICAgIDxzZWN0aW9uIGNvZGU9IjQ4NzY4LTYiIGRpc3BsYXlOYW1lPSJQQVlNRU5UIFNPVVJDRVMiPg0KICAgICAgICAgICAgPGl0ZW0geHNpOnR5cGU9IlBRIiBuaWQ9IkhEU0QwMC4wNC4wMTEiIG5hbWU9IuWkhOaWueiNr+WTgemHkeminSIgdmFsdWU9IjEzMCIgdW5pdD0i5YWDIi8+DQogICAgICAgIDwvc2VjdGlvbj4NCiAgICA8L3N0cnVjdHVyZWRCb2R5Pg0KPC9DbGluaWNhbERvY3VtZW50Pg==";
        message = new String(Base64.decodeBase64(message), "UTF-8");
        System.out.println(message + "\n");
        
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = domBuilder.parse(new InputSource(new StringReader(message))); //new ByteArrayInputStream(message.getBytes())
        System.out.println(w3cDoc.getDocumentElement().getNodeName());
        NamedNodeMap nodeMap = w3cDoc.getDocumentElement().getAttributes();
        for(int i=0; i<nodeMap.getLength(); i++) {
            System.out.println(nodeMap.item(i).getTextContent());
        }
        
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        saxParser.parse(new InputSource(new StringReader(message)), new DefaultHandler() {
            private String tagName;
            @Override
            public void startDocument() throws SAXException {
                System.out.println("start parsing document...");
            }
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                tagName = qName;
                System.out.println("parsing element " + qName);
                int attLen = attributes.getLength();
                if(attLen > 0) {
                    System.out.println("<" + tagName + "> attributes:");
                    for(int i=0; i<attLen; i++) {
                        System.out.println("    " + attributes.getQName(i) + "====" + attributes.getValue(i));
                    }
                }
            }
            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                String content = new String(ch, start, length);
                if(content.trim().length() > 0) {
                    System.out.println(tagName + "====" + content.trim());
                }
            }
        });
        
        SAXReader saxReader = getHL7V3Reader();
        Document doc = saxReader.read(new StringReader(message));
        String nsURI = doc.getRootElement().getNamespaceURI();
        String rootPrefix = "/" + doc.getRootElement().getName();
        String xpath = rootPrefix + "/structuredBody/section[@code='10160-0']/list[?]/entry[?]/item[@nid='HDSD00.04.023']/@value";
        xpath = getXMLNameSpaceFixed(xpath, nsURI);
        if(xpath.contains("list[?]") && xpath.contains("entry[?]")) {
            System.out.println("\nxpath contains list/entry");
        }
        String listPath = getLoopQueryPath(xpath, "list");
        List<Node> ListNodes = doc.selectNodes(listPath);
        for(Integer i=1; i<=ListNodes.size(); i++) {
            String entryPath = setXPathQueryValue(xpath, i.toString());
            String queryPath = getLoopQueryPath(entryPath, "entry");
            List<Node> EntryNodes = doc.selectNodes(queryPath);
            for(Integer j=1; j<=EntryNodes.size(); j++) {
                String realPath = setXPathQueryValue(entryPath, j.toString());
                System.out.println(realPath);
                Node valueNode = doc.selectSingleNode(realPath);
                System.out.println(valueNode.getText());
            }
        }
        
        String jsonStr = "{\"oid\":\"12306\",\"content\":[{\"id\":\"1\",\"cont\":\"aaa\"},{\"id\":\"2\",\"cont\":\"bbb\"}]}";
        String jsonAdd = "{\"id\":\"121\",\"cont\":\"abc\"}";
        JSONObject jobj = JSONObject.parseObject(jsonStr);
        System.out.println(jobj.get("content"));
        JSONObject jadd = JSONObject.parseObject(jsonAdd);
        JSONArray jarr = (JSONArray)jobj.get("content");
        jarr.add(jadd);
        System.out.println(jarr);
        
        
//        Driver mysqlDriver = new com.mysql.jdbc.Driver();
//        DriverManager.registerDriver(mysqlDriver);
        Class.forName("com.mysql.jdbc.Driver");
        String dbUrl = "jdbc:mysql://192.168.1.87:3306/jht_engine";
        String username="root";  String password="";
        Connection conn = DriverManager.getConnection(dbUrl, username, password);
        String sqlQuery = "SELECT * FROM engine_xpath_mapping WHERE target_table=?";
        PreparedStatement stmt = conn.prepareStatement(sqlQuery, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        stmt.setString(1, "patient_basic");
        ResultSet rs = stmt.executeQuery();
        RowSetFactory rsFactory = RowSetProvider.newFactory();
        CachedRowSet cachedRs = rsFactory.createCachedRowSet();
        cachedRs.populate(rs);
        rs.close();
        conn.close();
        while(cachedRs.next()) {
            System.out.println(cachedRs.getObject("target_column"));
        }
        File tmpfile = File.createTempFile("index", ".txt");
        System.out.println(tmpfile.getName());
        System.out.println(tmpfile.canWrite());
        String outputLine = "engine_xpath_mapping reference sharedocxpath";
        FileOutputStream fos = new FileOutputStream(tmpfile);
        PrintWriter bw = new PrintWriter(new OutputStreamWriter(fos));
        bw.write(outputLine);
        bw.flush();
        bw.close();
        
//        while(rs.next()) {
//            System.out.println(rs.getObject("TARGET_COLUMN"));
//            System.out.println(rs.getRow());
//        }
//        while(rs.previous()) {
//            System.out.println(rs.getObject("TARGET_COLUMN"));
//        }
//        System.out.println(rs.getRow());
//        rs.close();
//        stmt.close();
//        String sqlCall = "{call addNum(?, ?, ?)}";
//        CallableStatement ctmt = conn.prepareCall(sqlCall);
//        ctmt.setInt(1, 5);
//        ctmt.setInt(2, 3);
//        ctmt.registerOutParameter(3, Types.INTEGER);
//        ctmt.execute();
//        System.out.println(ctmt.getInt(3));
//        ctmt.close();
        
        FutureTask<Integer> task = new FutureTask<Integer>(new JLTest());
        new Thread(task, "returnValueThread").start();
        System.out.println("returnValueThread's return " + task.get());
        System.out.println(Thread.currentThread().getName() + " running task...");
        
        try {
            int i = 1/0;
            System.out.println(i);
            System.out.println(System.nanoTime());
        } catch(Exception e) {
            System.out.println("================================");
            e.printStackTrace();
        }
    }
    
    @Override
    public Integer call() {
        int i = 0;
        for(; i<10; i++) {
            System.out.println(Thread.currentThread().getName() + " running task " + i);
        }
        return i;
    }
    
    private static String getLoopQueryPath(String xpath, String query) {
        if(xpath.contains(query + "[?]")) {
            int pos = xpath.indexOf(query + "[?]");
            xpath = xpath.substring(0, pos) + query;
        }
        return xpath;
    }
    
    private static String setXPathQueryValue(String xpath, Object value) {
        return xpath.replaceFirst("\\?", value.toString());
    }

    private static String getXMLNameSpaceFixed(String xpath, String nsURI) {
        String nsPrefix = nsURI.contains("sc-wst") ? "sct" : "mif";
        String[] temps = xpath.split("[/]");
        StringBuilder sbXPath = new StringBuilder();
        for (String temp : temps) {
            if(StringUtils.isEmpty(temp)) {
                continue;
            }
            if(temp.indexOf("::") > 0) {
                String[] tt = temp.split("::");
                sbXPath.append("/" + tt[0] + "::" + nsPrefix + ":" + tt[1]);
            } else if (!temp.endsWith("()") && !temp.startsWith("@")) {
                sbXPath.append("/" + nsPrefix + ":" + temp);
            } else {
                sbXPath.append("/" + temp);
            }
        }
        return "/" + sbXPath.toString().substring(5);
    }
    
    private static SAXReader getHL7V3Reader() {
        SAXReader reader = new SAXReader(new DocumentFactory());
        Map<String, String> map = new HashMap<String, String>();
        map.put("sct", "urn:sc-wst:v2");
        map.put("mif", "urn:hl7-org:v3");
        map.put("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        reader.getDocumentFactory().setXPathNamespaceURIs(map);
        return reader;
    }

}
