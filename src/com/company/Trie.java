package com.company;

import java.util.HashMap;

public class Trie {
    private TrieNode root;

    public static class TrieNode {
        private static int CODE_POINT_INDEX = 0;
        public HashMap<Character, TrieNode> children;
        public int codePoint;
        public TrieNode() {
            children = new HashMap<>();
            codePoint = CODE_POINT_INDEX++;
        }
    }

    public Trie() {
        root = new TrieNode();
        for(int i = 0; i < 128; ++i) {
            char c = (char)i;
            root.children.put(c, new TrieNode());
        }
    }
    public TrieNode getRoot() {
        return root;
    }
}
