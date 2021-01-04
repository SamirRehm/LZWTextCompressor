package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static void encode() throws IOException {
        DataOutputStream output = new DataOutputStream(new FileOutputStream("myData.dat"));
        Trie t = new Trie();
        String str = new String(Files.readAllBytes(Paths.get("C:\\Users\\Samir\\IdeaProjects\\LZWTextCompressor\\src\\com\\company\\Harry Potter and the Sorcerer's Stone.txt")));
        char[] chars = str.toCharArray();
        int idx = 0;
        List<Integer> codePoints = new ArrayList<>();
        while (idx < chars.length) {
            Trie.TrieNode v = t.getRoot();
            char K = chars[idx];
            while (v.children.containsKey(K)) {
                v = v.children.get(K);
                idx++;
                if (idx == chars.length) break;
                K = chars[idx];
            }
            codePoints.add(v.codePoint);
            if (idx < chars.length) {
                v.children.put(K, new Trie.TrieNode());
            }
        }
        for (int i : codePoints) {
            byte lowByte = (byte) (i & 0xFF);
            output.writeByte(lowByte);
            byte midByte = (byte) ((i >> 8) & 0xFF);
            output.writeByte(midByte);
            byte highByte = (byte) ((i >> 16) & 0xFF);
            output.writeByte(highByte);
        }
        output.flush();
        output.close();
    }

    public static void decode() throws IOException {
        DataInputStream input = new DataInputStream(new FileInputStream("myData.dat"));
        List<Integer> codes = new ArrayList<>();
        while (input.available() > 0) {
            byte lowByte = input.readByte();
            byte midByte = input.readByte();
            byte highByte = input.readByte();
            int i = ((0xFF & 0) << 24) | ((0xFF & highByte) << 16) |
                    ((0xFF & midByte) << 8) | (0xFF & lowByte);
            codes.add(i);
        }
        HashMap<Integer, String> codeToString = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        int tableIndex = 1;
        for (int i = 0; i < 128; ++i) {
            char c = (char) i;
            codeToString.put(tableIndex, String.valueOf(c));
            tableIndex++;
        }
        int code = codes.get(0);
        String curr = codeToString.get(code);
        sb.append(curr);
        for (int i = 1; i < codes.size(); ++i) {
            String prev = curr;
            code = codes.get(i);
            if (code == tableIndex) {
                curr = prev + prev.charAt(0);
            } else {
                curr = codeToString.get(code);
            }
            sb.append(curr);
            codeToString.put(tableIndex, prev + curr.charAt(0));
            tableIndex++;
        }
        System.out.println(sb.toString());
        input.close();
    }

    public static void main(String[] args) throws IOException {
        encode();
        decode();
    }
}
