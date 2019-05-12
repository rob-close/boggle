package boggle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Boggle {

    public static void main(String[] args) {
        // Dictionary words from https://github.com/dwyl/english-words
        Dictionary dictionary = new Dictionary("/Users/rob/Downloads/english-words-master/words_alpha.txt");

        Boggle boggle = new Boggle(dictionary);
        List<Letter> letterList = boggle.buildGrid("abcdefghijklmnopqrstuvwxyz", 4);
//        List<Letter> letterList = boggle.buildGrid("abcdefghijklmnopqrstuvwxyz", 4);
//        List<Letter> letterList = boggle.buildGrid("antsseascatsdogsants", 4);

        long start = System.currentTimeMillis();
        boggle.play(letterList);
        long end = System.currentTimeMillis();
        System.out.println("Elapsed time (ms): " + (end-start));
    }

    private Dictionary dictionary;

    public Boggle(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    void play(List<Letter> letterList) {
        for ( Letter l : letterList) {
            List<Letter> word = new ArrayList<>();
            word.add(l);
            play(l, word);
        }
    }

    private void print(List<Letter> word) {
        for ( Letter l : word) {
            System.out.print(l.letter);
        }
        System.out.println();
    }

    private boolean contains( List<Letter> word, Letter letter) {
        for ( Letter l : word) {
            if (l == letter) {
                return true;
            }
        }
        return false;
    }

    private void play(Letter letter, List<Letter> word) {
        if (letter == null || letter.neighbours == null) return;
        for (Letter l : letter.neighbours) {
            if (!contains(word, l)) {
                word.add(l);
                if (word.size() > 2 && dictionary.lookUp(word)) {
                    print(word);
                }
                if (word.size() < dictionary.longestWord) {
                    play(l, word);
                }
                word.remove(word.size() - 1);
            }
        }
    }

    private List<Letter> buildGrid(String letters, int size) {
        int letterCount = size * size;
        if (letters.length() < letterCount) {
            throw new IllegalStateException("Not enough letters to play");
        }

        List<Letter> letterList = new ArrayList<>(letterCount);
        for (char c : letters.toCharArray()) {
            letterList.add(new Letter(c));
        }
        assignNeighbours(letterList, size);
        return letterList;
    }

    private void assignNeighbours(List<Letter> letterList, int size) {
        int row = 0;
        int col = 0;
        int end = size - 1;
        int N, NW, NE, S, SW, SE, E, W ;
        for (int i = 0, count = size * size; i < count; i++) {
            N = (row - 1) * size + col;
            NW = N - 1;
            NE = N + 1;
            S = (row + 1) * size + col;
            SW = S - 1;
            SE = S + 1;
            E = i + 1;
            W = i - 1;
            if (row == 0) {
                if (col == 0) {
                    letterList.get(i).neighbours = new ArrayList<>(3);
                    letterList.get(i).neighbours.add(letterList.get(E));
                    letterList.get(i).neighbours.add(letterList.get(SE));
                    letterList.get(i).neighbours.add(letterList.get(S));
                } else if (col == end) {
                    letterList.get(i).neighbours = new ArrayList<>(3);
                    letterList.get(i).neighbours.add(letterList.get(S));
                    letterList.get(i).neighbours.add(letterList.get(SW));
                    letterList.get(i).neighbours.add(letterList.get(W));
                } else {
                    letterList.get(i).neighbours = new ArrayList<>(5);
                    letterList.get(i).neighbours.add(letterList.get(E));
                    letterList.get(i).neighbours.add(letterList.get(SE));
                    letterList.get(i).neighbours.add(letterList.get(S));
                    letterList.get(i).neighbours.add(letterList.get(SW));
                    letterList.get(i).neighbours.add(letterList.get(W));
                }
            } else if (row == end) {
                if (col == 0) {
                    letterList.get(i).neighbours = new ArrayList<>(3);
                    letterList.get(i).neighbours.add(letterList.get(N));
                    letterList.get(i).neighbours.add(letterList.get(NW));
                    letterList.get(i).neighbours.add(letterList.get(E));
                } else if (col == end) {
                    letterList.get(i).neighbours = new ArrayList<>(3);
                    letterList.get(i).neighbours.add(letterList.get(W));
                    letterList.get(i).neighbours.add(letterList.get(NW));
                    letterList.get(i).neighbours.add(letterList.get(N));
                } else {
                    letterList.get(i).neighbours = new ArrayList<>(5);
                    letterList.get(i).neighbours.add(letterList.get(NE));
                    letterList.get(i).neighbours.add(letterList.get(E));
                    letterList.get(i).neighbours.add(letterList.get(W));
                    letterList.get(i).neighbours.add(letterList.get(NW));
                    letterList.get(i).neighbours.add(letterList.get(N));
                }
            } else {
                if (col == 0) {
                    letterList.get(i).neighbours = new ArrayList<>(5);
                    letterList.get(i).neighbours.add(letterList.get(NE));
                    letterList.get(i).neighbours.add(letterList.get(E));
                    letterList.get(i).neighbours.add(letterList.get(SE));
                    letterList.get(i).neighbours.add(letterList.get(S));
                    letterList.get(i).neighbours.add(letterList.get(NE));
                } else if (col == end) {
                    letterList.get(i).neighbours = new ArrayList<>(5);
                    letterList.get(i).neighbours.add(letterList.get(S));
                    letterList.get(i).neighbours.add(letterList.get(SW));
                    letterList.get(i).neighbours.add(letterList.get(W));
                    letterList.get(i).neighbours.add(letterList.get(NW));
                    letterList.get(i).neighbours.add(letterList.get(N));
                } else {
                    letterList.get(i).neighbours = new ArrayList<>(8);
                    letterList.get(i).neighbours.add(letterList.get(NE));
                    letterList.get(i).neighbours.add(letterList.get(E));
                    letterList.get(i).neighbours.add(letterList.get(SE));
                    letterList.get(i).neighbours.add(letterList.get(S));
                    letterList.get(i).neighbours.add(letterList.get(SW));
                    letterList.get(i).neighbours.add(letterList.get(W));
                    letterList.get(i).neighbours.add(letterList.get(NW));
                    letterList.get(i).neighbours.add(letterList.get(N));
                }
            }
            col++;
            if (col > end) {
                row++;
                col = 0;
            }
        }
    }

    private class Letter {
        Letter(char c) {
            letter = c;
        }
        char letter;
        List<Letter> neighbours;
    }

    static class Dictionary {
        TrieNode root;
        int longestWord = 0;

        Dictionary(String fileName) {
            root = new TrieNode('\0');
            load(fileName);
        }

        private void load(String fileName) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line = reader.readLine();
                while (line != null) {
                    if (line .length() > 2) {
                        add(line);
                        longestWord = Math.max(longestWord, line .length());
                    }
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void add(String word) {
            TrieNode node = root;

            for (char c : word.toCharArray()) {
                int i = c - 'a';
                if (node.next[i] == null) {
                    node.next[i] = new TrieNode(c);
                    node.nextCount++;
                }

                node = node.next[i];
            }
            node.isWordEnd = true;
        }

        boolean lookUp(List<Letter> letters) {
            TrieNode node = root;

            for (Letter l : letters) {
                int i = l.letter - 'a';
                if (node.next[i] == null) {
                    return false;
                }

                node = node.next[i];
            }
            return (node != null && node.isWordEnd);
        }

        static class TrieNode {
            TrieNode(char value) {
                this.value = value;
            }

            char value;
            boolean isWordEnd;

            // Each element in the array represents a letter in the alphabet.
            TrieNode[] next = new TrieNode[26];
            int nextCount = 0;
        }
    }
}
