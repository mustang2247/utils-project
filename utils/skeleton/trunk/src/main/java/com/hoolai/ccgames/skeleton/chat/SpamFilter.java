package com.hoolai.ccgames.skeleton.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @version Date:2009-2-3 以森林数据结构作为词典，负责构建词典和接受查询调用返回替换结果
 */
public class SpamFilter {

	/**
	 * 森林中树的集合
	 */
	private Map<Character, Tree> trees = new HashMap<Character, Tree>();

	/**
	 * 构造子，根据词典文件构建整个森林
	 * 
	 * @param fileName
	 */
	public SpamFilter(String fileName) {
		initialize(fileName);
	}

//	public SpamForest(File file) {
//		initialize(file);
//	}

	public void initialize(String fileName) {
		// 每当读入一句脏话就提取第一个字，检查这个字是否在森林的树根里存在，若不存在则创建新树
		BufferedReader bufferReader = null;
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream input = loader.getResourceAsStream(fileName);
			InputStreamReader inputStreamReader = new InputStreamReader(
					input, "UTF-8");
			bufferReader = new BufferedReader(inputStreamReader);
			String line = null;
			Character firstWord = null;
			while ((line = bufferReader.readLine()) != null) {
				if (line.length() != 0) {
					firstWord = line.toCharArray()[0];
					if (!trees.containsKey(firstWord)) {
						// 如果这个脏字在森林里不存在，那么创建新树
						Tree newTree = new Tree(line, firstWord);// 将用line构建树的一枝
						trees.put(firstWord, newTree); // 栽树
					} else {
						// 如果树已经存在，那么使用这句脏话使树生长
						trees.get(firstWord).growUp(line);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 替换trees
	 * 
	 * @author taoshuang XPEC
	 * @param trees
	 */
	public void swapTree(Map<Character, Tree> trees) {
		this.trees = trees;
	}

	/**
	 * 检测用户输入并且如果出现脏话就替换脏话字段为*
	 * 
	 * @param userInput
	 *            用户输入
	 * @return 替换好的脏话
	 */
	public String checkAndReplaceInput(String userInput) {
		char[] inputArray = userInput.toCharArray();
		String returnString = userInput;
		String returnedCheckedString;
		String travelString = null;
		Tree travelTree = null;
		int index = 0;

		while (index < inputArray.length) {
			if (trees.containsKey(inputArray[index])) {
				// 如果用户的输入中有字出现在森林里树的根中
				travelString = userInput.substring(index);
				travelTree = trees.get(inputArray[index]);// 获得这棵树

				if ((returnedCheckedString = travelTree
						.checkInput(travelString)) != null) {
					// 如果得到了脏话的匹配
					returnString = replace(returnString, returnedCheckedString,
							index);
					index = index + returnedCheckedString.length();// 从这句脏话的后面继续搜索
				} else {
					index++;
				}
			} else {
				index++;
			}
		}
		// 没有发现脏话返回原话
		return returnString;
	}

	/**
	 * 替换脏话字段
	 * 
	 * @param userInput
	 *            用户输入
	 * @param returnedCheckedString
	 *            脏话字段
	 * @param index
	 *            脏话字段在用户输入的位置
	 * @return
	 */
	private String replace(String userInput, String returnedCheckedString,
			int index) {
		int length = returnedCheckedString.length();
		char[] starArray = new char[length];
		for (int i = 0; i < length; i++) {
			starArray[i] = '*';
		}
		String repalceString = new String(starArray);
		if (index > 0) {
			return userInput.substring(0, index) + repalceString
					+ userInput.substring(index + length);
		} else {
			return repalceString + userInput.substring(length);
		}
	}
	
	
	public class Tree {
		
		/**
		 * 树根节点
		 */
		private TreeNode root = null;
		
		/**
		 * 这棵树的高度
		 */
		private int maxLevel = 0;
		
		/**
		 * 根内容
		 */
		private Character rootWord = null;
		
		/**
		 * 树的构造子
		 * @param sentence 脏话 
		 * @param firstWord 脏话首字
		 */
		public Tree(String sentence, Character firstWord) {
			rootWord = firstWord;
			root = new TreeNode(firstWord, false);
			root.setParent(null);
			travel(sentence);
		}
		
		/**
		 * 向外提供的方法，用一句脏话给树添枝加叶
		 * @param sentence 脏话
		 */
		public void growUp(String sentence) {
			travel(sentence);
		}
		
		/**
		 * 用一句脏话给树添枝加叶的实现
		 * @param sentence
		 */
		private void travel(String sentence) {
			char[] charArray = sentence.toCharArray();
			int depth = 1; //表示该句已走了多少步
			TreeNode footStep = root;
			while(depth < charArray.length) {
				//一个一个汉字去爬树，直到爬到顶，也就是charArray的最后一个字
				if((footStep.getChildNodes()).containsKey((Character)charArray[depth])) {
					//判断该节点的子节点中存在不存在这个汉字的节点
					footStep = footStep.getChildNodes().get(charArray[depth]);//将当前节点置为此子节点			
				}
				else {
					//子节点中不存在此汉字，则创建该子节点，并加入到子节点集合中
					TreeNode newNode = new TreeNode(charArray[depth], false);//申请一个新的中间节点
					newNode.setParent(footStep);
					footStep.getChildNodes().put((Character)charArray[depth], newNode);//将新的节点插入到当前节点的子节点集合中
					footStep = newNode;
				}
				if(depth > maxLevel) {
					maxLevel = depth;
				}
				depth++;//同时字串位置后移
			}
			if(depth == charArray.length) {
				//如果达到了句子末尾就在该节点的sentence域存储这句脏话
				footStep.setSentence(sentence);
			}
		}
		
		/**
		 * 检查用户输入是否含有脏话
		 * @param travelString 实际用户输入
		 * @return 返回脏话部分
		 */
		public String checkInput(String travelString) {
			int depth = 1;
			String badSentence = null;
			int stringLength = travelString.length();
			char[] charArray = new char[stringLength + 1];
			travelString.getChars(0, stringLength, charArray, 0);
			charArray[stringLength] = ' ';
			TreeNode footStep = root;
			TreeNode footParent = null;
			
			while(depth < charArray.length) {
				//让这句输入爬树，爬到第一个在树上不存在字的位置。
				//检查当前节点是否为叶子节点，若是则返回脏话，不是返回null
				if(footStep.getChildNodes().containsKey(charArray[depth])) {
					//如果在当前位置的子节点中存在这个字，则继续爬
					footStep = footStep.getChildNodes().get(charArray[depth]);
					depth++;
				}
				else {
					//在当前位置的子节点中不存在这个字
					if((badSentence = footStep.getSentence()) != null) {
						//说明这是一个叶子节点
						return badSentence;
					}
					else {
						//在当前位置的子节点中不存在这个字并且当前位置也不是叶子节点
						//回溯到这条枝干上叶子
						while((footParent = footStep.getParent()) != null) {
							if(footParent.getSentence() != null) {
								return badSentence = footParent.getSentence();
							}
							footStep = footStep.getParent();
						}
						badSentence = footStep.getSentence();
						break;
					}
				}
			}
			return badSentence;
		}

		/**
		 * 获取此树的根内容
		 * @return 根内容
		 */
		public Character getRootWord() {
			return rootWord;
		}
		
		
		/**
		 * @version Date:2009-2-3
		 * 枝叶数据结构，负责构建节点和保存数据
		 */
		public class TreeNode {
			
			/**
			 * 父亲节点
			 */
			private TreeNode parent = null;
			
			/**
			 * 子节点集合，key是每个子节点的hash code
			 */
			private Map<Character, TreeNode> children = null;
			
			/**
			 * 本节点value
			 */
			private Character wordValue;
			
			
			/**
			 * 标志是否叶子节点 
			 */
			private boolean isLeaf = false;
			
			/**
			 * 如果是叶子节点，存储脏话
			 */
			private String badSentence = null;
			
			public TreeNode(Character word, boolean isLeaf) {
				this.wordValue = word;
				this.isLeaf = isLeaf;
				children = new HashMap<Character, TreeNode>();
			}
			
			public TreeNode(String badSentence) {
				this.isLeaf = true;
				this.badSentence = badSentence;
			}
			
			/**
			 * 获得所有子节点的集合
			 * @return 子节点集合
			 */
			public Map<Character, TreeNode> getChildNodes() {
				return children;
			}
			
			/**
			 * 判断是否为叶子节点
			 * @return 是否为叶子节点 
			 */
			public boolean getIsLeaf() {
				return isLeaf;
			}
			
			/**
			 * 置badSentence域，并且使该节点有叶子的身份
			 * @param sentence 脏话
			 */
			public void setSentence(String sentence) {
				this.badSentence = sentence;
				this.isLeaf = true;
			}
			
			/**
			 * 获得该叶子节点的脏话
			 * @return 脏话
			 */
			public String getSentence() {
				return this.badSentence;
			}
			
			/**
			 * 获得该节点的字域
			 * @return 字
			 */
			public Character getWordValue() {
				return wordValue;
			}

		     /**
			 * 获得该节点的父亲节点
			 * @return parent
			 */
			public TreeNode getParent() {
				return parent;
			}

			/**
			 * 设置该节点的父亲节点
			 * @param parent
			 */
			public void setParent(TreeNode parent) {
				this.parent = parent;
			}
		}
	}
	
}
