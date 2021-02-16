import java.util.ArrayList;
import java.util.List;
//name1: Tamir Kashi
//ID1: 207481137
//username1: tamirizhark

//name2: Adva Helman
//ID2: 206087900
//username2: advahelman

/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */

public class AVLTree 
{
  protected IAVLNode root;
  protected int size;
  protected IAVLNode min_Avl;
  protected IAVLNode max_Avl;

	public AVLTree() 
	{
		this.size=0;
		this.root= new FakeAVLNode();
		min_Avl =root;
		max_Avl=root;
	}

/**
   * public boolean empty()
   *
   * returns true if and only if the tree is empty
   *
   */
//complexity O(1)
  public boolean empty() 
  {
	  if(size()==0)
	  {
		  return true;
	  }
    return false;
  }

 /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   */
//complexity O(logn)
  public String search(int k)
  {
	  IAVLNode node = this.getRoot();
	  return this.searchNode(k, node);
  }
//complexity O(logn)
  protected String searchNode(int k, IAVLNode node) 
  {
	  int key = node.getKey();
	  if(key==k)//found the node 
	  {
		  return node.getValue();
	  }
	  else 
	  {
		  if(k>key) 
		  {
			  if(!node.isRealNode()) 
			  {
				 return null; 
			  }
			  else 
			  {
				  IAVLNode right = node.getRight();
				  if(right.isRealNode()) 
				  {
					  return this.searchNode(k,right);
				  }
				  else 
				  {
					 return null; 
				  }
			  }
		  }
		  else //k<key 
		  {
			  if(!node.isRealNode()) 
			  {
				 return null; 
			  }
			  else 
			  {
				  IAVLNode left = node.getLeft();
				  if(left.isRealNode()) 
				  {
					  return this.searchNode(k,left);
				  }
				  else 
				  {
					 return null; 
				  }  
			  } 
		  }
	  }
  }


  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the AVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * promotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
   * returns -1 if an item with key k already exists in the tree.
   */
//complexity O(logn)

  public int insert(int k, String i) 
  {
	   int number_rebalnce;
	   if(search(k)!=null)
	   {
		   return -1;
	   }
	   else 
	   {
		   size++;
		   number_rebalnce =  insert_Node(this.getRoot(),k,i);//inserting the new node like in BST
	   }   

	  return number_rebalnce;
  }
  
  private IAVLNode searchWhereToInsert(IAVLNode root2, int k,String i) ///return the parent that under him we want to insert
  {
	  
	  if(!root2.isRealNode()) 
	  {
		  return root2.getParent();
	  }
	  if(!root2.getLeft().isRealNode() &&  k<root2.getKey()) 
	  {
		  return root2;
	  }
	  else if(!root2.getRight().isRealNode() &&  k>root2.getKey()) 
	  {
		  return root2;
	  }
	  else if (root2.getKey() > k) 
	   {
	  		return searchWhereToInsert(root2.getLeft(),k , i);
	   } 
	   else 
	   {
		   return searchWhereToInsert(root2.getRight(),k , i);
	   }
	  
  }
  
  
 private int insert_Node(IAVLNode root2, int k,String i)  
 {
	   int counter=0;
	   AVLNode node = new AVLNode(k,i);
	   
	   if(!root2.isRealNode() && root2.getParent()==null) //tree is empty case
	   {
		   this.root=node;
		   min_Avl=node;
		   max_Avl=node;
		   //counter += Rebalance(node,counter);//take care the cases in the top of the tree  
		   return counter;
	   }
	   IAVLNode parent = searchWhereToInsert(root2, k, i);
	   if(parent.getKey()>k) {
		   parent.setLeft(node);
		   node.setParent(parent);
	   }
	   else 
	   {
		   parent.setRight(node);
		   node.setParent(parent);
	   }
	   updateSizeNode(parent);
	   if(k<min_Avl.getKey()) 
	   {//update if k of the new node is min in the tree
		   min_Avl=node;
	   }else if(k>max_Avl.getKey()) {
		   max_Avl=node;
	   }
	   counter+=Rebalance(node,counter);
	   return counter;

}
//complexity O(logn)

 int Rebalance(IAVLNode root2, int counter) 
 {   //take care of the balance after insert 
	   while(root2.getParent()!=null) 
	   {
		   root2 = root2.getParent();
		   int leftBalance = root2.getHeight()-root2.getLeft().getHeight();
		   int rightBalance = root2.getHeight()-root2.getRight().getHeight();
		   if((leftBalance==0 && rightBalance==1) ||(leftBalance==1 && rightBalance==0)) ///CASE1
		   {
			   root2.setHeight(root2.getHeight()+1);
			   counter++;
			   continue;
		   }
		   else if(leftBalance == 0 && rightBalance==2) 
		   {
			   IAVLNode leftSon = root2.getLeft();
			   int leftSonleftBalance = leftSon.getHeight()-leftSon.getLeft().getHeight();
			   int leftSonrightBalance = leftSon.getHeight()-leftSon.getRight().getHeight();
			   if(leftSonleftBalance==1 && leftSonrightBalance==2)//CASE2 
			   {
				   rightRotate(root2);
				   counter=counter+5;
				   break;
			   }
			   else if (leftSonleftBalance==2 && leftSonrightBalance==1)//CASE3
			   {
				   IAVLNode fakeRoot2 = root2;
				   leftRotate(leftSon);
				   rightRotate(fakeRoot2);
				   counter=counter+10;
				   break;
			   }
		   }
		   else if (leftBalance == 2 && rightBalance== 0) 
		   {
			   IAVLNode rightSon = root2.getRight();
			   int rightSonleftBalance = rightSon.getHeight()-rightSon.getLeft().getHeight();
			   int rightSonrightBalance = rightSon.getHeight()-rightSon.getRight().getHeight();
			   if(rightSonleftBalance==1 && rightSonrightBalance==2)//CASE3 
			   {
				   IAVLNode fakeRoot2 = root2;
				   rightRotate(rightSon);
				   leftRotate(fakeRoot2);
				   counter=counter+10;
				   break;
			   }
			   else if (rightSonleftBalance==2 && rightSonrightBalance==1)//CASE2
			   {
				   leftRotate(root2);
				   counter=counter+5;
				   break;
			   }
		   }
	   }
	   return counter;
 } 

//complexity O(logn)
 private int deleteRebalance (IAVLNode node, int counter) 
 {//take care to rebalance the Avl after delete
	 while(node!=null) 
	   {
		   int leftBalance = node.getHeight()-node.getLeft().getHeight();
		   int rightBalance = node.getHeight()-node.getRight().getHeight();
			if((leftBalance==2 && rightBalance==1) || (leftBalance==2 && rightBalance==1)) //NO PROBLEM
			{
				 //break;
			}
			else if (leftBalance==2 && rightBalance==2) //CASE1
			{
				 updateHeight(node);
				 counter++;
				 node = node.getParent();
				 continue;
			}
			else if ((leftBalance==3 && rightBalance==1) || (leftBalance==1 && rightBalance==3)) //3-1 or 1-3 cases
			{
				 if((leftBalance==3 && rightBalance==1)) ////3-1 case
				 {
					 IAVLNode righttSon = node.getRight();
					 int rightSonleftBalance = righttSon.getHeight()-righttSon.getLeft().getHeight();
					 int rightSonrightBalance = righttSon.getHeight()-righttSon.getRight().getHeight();
					 if(rightSonleftBalance==1 && rightSonrightBalance==1) //CASE2
					 {
						 leftRotate(node);
						 counter=counter+5;
					 }
					 else if(rightSonleftBalance==2 && rightSonrightBalance==1)//CASE3 
					 {
						 leftRotate(node);
						 counter=counter+5;

					 }
					 else if(rightSonleftBalance==1 && rightSonrightBalance==2)//CASE4
					 {
						 IAVLNode fakenode = node;
						 rightRotate(righttSon);
						 leftRotate(fakenode);
						 counter=counter+10;

					 }
				 }
				 else if((leftBalance==1 && rightBalance==3)) ////1-3 case
				 {
					 IAVLNode leftSon = node.getLeft();
					 int leftSonleftBalance = leftSon.getHeight()-leftSon.getLeft().getHeight();
					 int leftSonrightBalance = leftSon.getHeight()-leftSon.getRight().getHeight();
					 if(leftSonleftBalance==1 && leftSonrightBalance==1) //CASE2
					 {
						 rightRotate(node);
						 counter=counter+5;

					 }
					 else if(leftSonleftBalance==1 && leftSonrightBalance==2)//CASE3 
					 {
						 rightRotate(node);
						 counter=counter+5;

					 }
					 else if(leftSonleftBalance==2 && leftSonrightBalance==1)//CASE4
					 {
						 IAVLNode fakenode = node;
						 leftRotate(leftSon);
						 rightRotate(fakenode);
						 counter=counter+10;

					 }
				 }
			}
			node = node.getParent();
	   }
	   return counter;
 }
 
//complexity O(1)
	private void updateHeight(IAVLNode root2) //update the height of a node 
	{

		if((root2.getLeft().isRealNode()) && (!root2.getRight().isRealNode())) 
		{
			root2.setHeight(1 +root2.getLeft().getHeight());
			return;
		}
		else if((!root2.getLeft().isRealNode()) && (root2.getRight().isRealNode())) 
		{
			root2.setHeight(1 +root2.getRight().getHeight());
			return;
		}
		else if ((!root2.getLeft().isRealNode()) && (!root2.getRight().isRealNode())) {
			root2.setHeight(0);
		}
		else 
		{
			root2.setHeight(1 + Math.max(root2.getLeft().getHeight(), root2.getRight().getHeight())); 
		}
	}

  /**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * demotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
   * returns -1 if an item with key k was not found in the tree.
   */
	//complexity O(logn)

   public int delete(int k)
   {
	   IAVLNode node = this.getRoot();
	   return this.deleteNode(k, node);
   }

   public int deleteNode(int k, IAVLNode node) 
   {
	   int counter =0; //rebalancing operations counter
       String search = this.search(k);
	   if (search==null)//check if k is in the tree
	   {
		   return -1;
	   }
	   if (node.getKey() == k)//found the node that need to be deleted
	   {
		   this.size=this.size-1;
		   if (((node.getLeft().isRealNode()==false) && (node.getRight().isRealNode()==false))) //the node to be deleted is a leaf
		   {
			   if(node.getParent()==null)//node is the only AVLNode in the tree 
			   {
				   this.root=new FakeAVLNode();
			   }
			   else 
			   {
				   IAVLNode parent = node.getParent();
				   if(parent.getLeft().getKey()==node.getKey()) //node to be deleted is a left child of its parent
				   {
					   parent.setLeft( new FakeAVLNode());
					   deleteRebalance(parent, counter);
					   updateSizeNode(parent);
				   }
				   else //node to be deleted is a right child of its parent
				   {
					   parent.setRight( new FakeAVLNode());
					   deleteRebalance(parent, counter);
					   updateSizeNode(parent);
				   }
			   }
		   }
		   else if (((node.getLeft().isRealNode()==false) && (node.getRight().isRealNode()==true)) || ((node.getLeft().isRealNode()==true) && (node.getRight().isRealNode()==false))) //the node to be deleted is an unary node
		   {
			   IAVLNode son = node.getLeft(); //son is the son of the node that needs to be deleted
			   if(!son.isRealNode()) 
			   {
				   son = node.getRight();
			   }
			   if(node.getParent()!=null) 
			   {
				   IAVLNode parent = node.getParent();
				   if(parent.getLeft().getKey()==node.getKey()) //node to be deleted is a left child of its parent
				   {
					   parent.setLeft(son);
					   son.setParent(parent);
					   deleteRebalance(parent, counter);
					   updateSizeNode(parent);

				   }
				   else //node to be deleted is a right child of its parent
				   {
					   parent.setRight(son);
					   son.setParent(parent);
					   deleteRebalance(parent, counter);
					   updateSizeNode(parent);

				   }
			   }
			   else 
			   {
				   son.setParent(null);
				   this.root=son;
				   deleteRebalance(this.root, counter);
			   }
		   }
		   else if ((node.getLeft().isRealNode()==true) && (node.getRight().isRealNode()==true)) //the node to be deleted is a binary node
		   {
			   IAVLNode maxNode = findMaximum(node.getLeft()); //find the predecessor of the node that need to be deleted
			   this.delete(maxNode.getKey());///check if this is ok, recursive call to delete in order to delete minNode
			   maxNode.setRight(node.getRight());
			   maxNode.getRight().setParent(maxNode);
			   if(node.getLeft().getKey()!=maxNode.getKey()) 
			   {
				   maxNode.setLeft(node.getLeft());
				   maxNode.getLeft().setParent(maxNode);
			   }
			   else 
			   {
				   FakeAVLNode Fake = new FakeAVLNode();
				   maxNode.setLeft(Fake);
			   }
			   maxNode.setParent(node.getParent());
			   if(node.getParent()!=null) 
			   {
				   IAVLNode parent = node.getParent();

				   if(parent.getLeft().getKey()==node.getKey()) 
				   {
					   node.getParent().setLeft(maxNode);
				   }
				   else 
				   {
					   node.getParent().setRight(maxNode);
				   }
				   updateSizeNode(parent);
			   }
			   else 
			   {
				   maxNode.setParent(null);
				   this.root=maxNode;
			   }
			   counter++;
			   deleteRebalance(maxNode,counter);
			   node.setParent(null);
		   }
	   }
	   else 
	   {
		 if (k<node.getKey()) //if k is smaller than current node, recursively delete for current node.left
		 {
			return deleteNode(k,node.getLeft()); 
		 }
		 if (k>node.getKey()) //if k is bigger than current node, recursively delete for current node.right
		 {
			return deleteNode(k,node.getRight()); 
		 }
	   }
	   min_Avl= findMinimum(this.root);
	   max_Avl=findMaximum(this.root);
	   return -1; //not meant to get here
   }
	//complexity O(logn)
   private void updateSizeNode(IAVLNode node)  //node is the parent of the node that was deleted/inserted.
   {
	   if(node==null) 
	   {
		   return;
	   }
	   IAVLNode temp = node;
	   IAVLNode root = this.root;
	   int key=node.getKey();
	   int rootkey = this.root.getKey();
	   while(key!=rootkey) 
	   {
		   if(temp==null) 
		   {
			   return;
		   }
		   if(!temp.isRealNode()) 
		   {
			   temp.setSize(0);
		   }
		   else 
		   {
			   temp.setSize(temp.getLeft().getSize()+temp.getRight().getSize()+1);
		   }
		   temp = temp.getParent();
		   if(temp!=null) 
		   {
			   key=temp.getKey();
		   }
	   }
	   root.setSize(root.getLeft().getSize()+root.getRight().getSize()+1);
   }
	//complexity O(1)
   private void leftRotate(IAVLNode node) // left-rotate node
   {
       node.setHeight(node.getHeight()-1);
       if(node==this.getRoot())
       {
           this.root = node.getRight();
           node.getRight().setParent(null);
       }
       IAVLNode right = node.getRight();
       IAVLNode leftOfRight = right.getLeft();
       IAVLNode nodesParent;
       if (node.getParent() != null){
           nodesParent = node.getParent();
           right.setParent(nodesParent);
           if (right.getKey() > nodesParent.getKey()) {
               nodesParent.setRight(right);
           }
           else {
               nodesParent.setLeft(right);
           }
       }
       right.setLeft(node);
       node.setRight(leftOfRight);
       if(leftOfRight!=null)
       {
           leftOfRight.setParent(node);
       }
       right.setParent(node.getParent());
       node.setParent(right);
       this.updateHeight(node);
       if(node.getLeft().isRealNode())
       {
           this.updateHeight(node.getLeft());
       }
       if(node.getRight().isRealNode())
       {
           this.updateHeight(node.getRight());
       }
       this.updateHeight(node.getParent());
       node.setSize(node.getLeft().getSize()+node.getRight().getSize()+1);
       right.setSize(right.getLeft().getSize()+right.getRight().getSize()+1);

   }
//complexity O(1)
   private void rightRotate(IAVLNode node) //right-rotate node
   {
       if(node==null)
       {
           return;
       }
       node.setHeight(node.getHeight()-1);
       if(node==this.getRoot())
       {
           this.root = node.getLeft();
           node.getLeft().setParent(null);
       }

       IAVLNode left = node.getLeft();
       IAVLNode RightOfLeft = left.getRight();

       IAVLNode nodesParent;
       if (node.getParent() != null){
           nodesParent = node.getParent();
           left.setParent(nodesParent);
           if (left.getKey() > nodesParent.getKey()) {
               nodesParent.setRight(left);
           }
           else {
               nodesParent.setLeft(left);
           }
       }

       left.setRight(node);
       node.setLeft(RightOfLeft);
       if(RightOfLeft!=null)
       {
           RightOfLeft.setParent(node);
       }
       left.setParent(node.getParent());
       node.setParent(left);
       this.updateHeight(node);
       if(node.getLeft().isRealNode()) {
           this.updateHeight(node.getLeft());
       }
       if(node.getRight().isRealNode()) {
           this.updateHeight(node.getRight());
       }
       this.updateHeight(node.getParent());
       node.setSize(node.getLeft().getSize()+node.getRight().getSize()+1);
       left.setSize(left.getLeft().getSize()+left.getRight().getSize()+1);

   }
   
	//complexity O(1)

   private boolean isRoot(IAVLNode node) //used in split check if the node is a root of the tree
   {
	   if(node.getParent()==null) 
	   {
		   return true;
	   }
	   return false;
   }
   /**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
	//complexity O(1)

   public String min()
   {
	   if(this.empty()==true) 
	   {
		   return null;
	   }
	  
	   return min_Avl.getValue();
   }
	//complexity O(logn)

   private IAVLNode findMinimum(IAVLNode iavlNode) 
   {//finding the minimum in the tree
	   if(!iavlNode.getLeft().isRealNode()) {
		   return iavlNode;
	   }
	   else {
		   return findMinimum(iavlNode.getLeft());
	   }
}

   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    * 
    */
//complexity O(1)
   public String max()
   {
	   if(this.empty()==true) 
	   {
		   return null;
	   }

	   return max_Avl.getValue();
   }

	//complexity O(logn)

   private IAVLNode findMaximum(IAVLNode iavlNode) 
   {//finding the maximum in the tree
	   if(!iavlNode.getRight().isRealNode()) {
		   return iavlNode;
	   }
	   else 
		   return findMaximum((AVLNode) iavlNode.getRight());
	
}
  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
	//complexity O(n)

   public int[] keysToArray()
   {
 	  int[] empty_arr = new int[0];
 	  if(empty()==true) 
 	  {
 		   return empty_arr;
 	  }
	   int counter=0;
 	  int[] arr = new int[this.size()];
 	  storeInOrder(this.getRoot(),arr,counter);
         return arr;            
   }
	//complexity O(n)

   private int storeInOrder(IAVLNode root2,int[] arr,int counter) 
   {
		  if(root2.getLeft().isRealNode()) 
		  {
			 counter= storeInOrder(root2.getLeft(),arr,counter);//recursively travel left
		  }
		  arr[counter]=root2.getKey();
		  counter++;
		  if(root2.getRight().isRealNode()) 
		  {
			  counter= storeInOrder(root2.getRight(),arr,counter);//recursively travel right
		  }
		
	  return counter;
 }

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
	//complexity O(n)

  public String[] infoToArray()
  {
	    int size = this.size();
	 	String[] empty_arr = new String[0];
        String[] arr = new String[size]; // array's length is tree's size
        if(empty()==true) //empty tree case
        {
        	return empty_arr;
        }
	  	int counter=0; // remarks where to insert in the array (index)

        innerInfoToArray(arr ,this.getRoot(),counter);      
        return arr;
  }
	//complexity O(n)

  private int innerInfoToArray(String[]arr, IAVLNode node,int counter) 
  {
		  if(node.getLeft().isRealNode()) 
		  {
			  counter = innerInfoToArray(arr, node.getLeft(),counter);//recursively travel left
		  }
		  arr[counter]=node.getValue();
		  counter++;
		  if(node.getRight().isRealNode()) 
		  {
			  counter= innerInfoToArray(arr, node.getRight(),counter);//recursively travel right
		  }
		
	  return counter;
  }
  
   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    */
	//complexity O(1)

   public int size()
   {
	   return this.size; 
   }
   
     /**
    * public int getRoot()
    *
    * Returns the root AVL node, or null if the tree is empty
    *
    * precondition: none
    * postcondition: none
    */
	//complexity O(1)

   public IAVLNode getRoot()
   {
	   return this.root;
   }
     /**
    * public string split(int x)
    *
    * splits the tree into 2 trees according to the key x. 
    * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	  * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
    * postcondition: none
    */   
	//complexity O(logn)

   public AVLTree[] split(int x) 
   {
	   IAVLNode node = searchForSplit(this,x);
	   AVLTree[]arr = new AVLTree[2];
	   AVLTree t1 = new AVLTree();
	   AVLTree t2 = new AVLTree();
	   t1.root = node.getLeft();
	   t1.size = t1.getRoot().getSize();
	   t2.root = node.getRight();
	   t2.size = t2.getRoot().getSize();
	   IAVLNode parent = node.getParent();
	   int nodeKey = node.getKey();
	   if(node.getParent()!=null) 
	   {
		   while(!isRoot(parent)) 
		   {
			   IAVLNode grandpa = parent.getParent();
			   AVLTree tempTree = new AVLTree();
			   if(parent.getRight().getKey()==nodeKey) //node is the Right son of its parent 
			   {
				   IAVLNode brother = parent.getLeft();
				   tempTree.root=brother;
				   tempTree.size = brother.getSize();
				   if(brother.isRealNode()) 
				   {
					   grandpa = parent.getParent();
					   t1.join(parent, tempTree);
				   }
				   else 
				   {
					   t1.insert(parent.getKey(), parent.getValue());
				   }
			   }
			   else//node is the left son of its parent 
			   {
				   IAVLNode brother = parent.getRight();
				   tempTree.root=brother;
				   tempTree.size = brother.getSize();
				   if(brother.isRealNode()) 
				   {
					   grandpa = parent.getParent();
					   t2.join(parent, tempTree);
				   }
				   else 
				   {
					   t2.insert(parent.getKey(), parent.getValue());
				   }
			   }
			   nodeKey = parent.getKey();
			   parent = grandpa;
		   }
		   AVLTree tempTree = new AVLTree();
		   if (parent.getKey()>x) 
		   {
			   tempTree.root=parent.getRight();
			   tempTree.size = parent.getRight().getSize();
			   t2.join(parent, tempTree);
		   }
		   else 
		   {
			   tempTree.root=parent.getLeft();
			   tempTree.size = parent.getLeft().getSize();
			   t1.join(parent, tempTree);
		   }
		   
	   }
	
	   arr[0] = t1;
	   arr[1] = t2;
	   t1.max_Avl=findMaximum(t1.getRoot());//updte maximum in t1
	   t1.min_Avl=findMinimum(t1.getRoot());//updte minimum in t1
	   t2.min_Avl= findMinimum(t2.getRoot());//updte minimum in t2
	   t2.max_Avl=findMaximum(t2.getRoot());//updte maximum in t2
	   
	   return arr; 
   }
   
   
   
   public IAVLNode searchForSplit(AVLTree tree, int k)
   {
 	  IAVLNode node = tree.getRoot();
 	  return tree.NodeSearchForSplit(k, node);
   }
   
   protected IAVLNode NodeSearchForSplit(int k, IAVLNode node) 
   {
 	  
 	  int key = node.getKey();
 	  if(key==k)//found the node 
 	  {
 		  return node;
 	  }
 	  else 
 	  {
 		  if(k>key) 
 		  {
 			  if(!node.isRealNode()) 
 			  {
 				 return null; 
 			  }
 			  else 
 			  {
 				  IAVLNode right = node.getRight();
 				  if(right.isRealNode()) 
 				  {
 					  return NodeSearchForSplit(k,right);
 				  }
 				  else 
 				  {
 					 return null; 
 				  }
 			  }
 		  }
 		  else //k<key 
 		  {
 			  if(!node.isRealNode()) 
 			  {
 				 return null; 
 			  }
 			  else 
 			  {
 				  IAVLNode left = node.getLeft();
 				  if(left.isRealNode()) 
 				  {
 					  return NodeSearchForSplit(k,left);
 				  }
 				  else 
 				  {
 					 return null; 
 				  }  
 			  } 
 		  }
 	  }
   }
   
   
	/**
	    * public join(IAVLNode x, AVLTree t)
	    *
	    * joins t and x with the tree. 	
	    * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
		  * precondition: keys(x,t) < keys() or keys(x,t) > keys(). t/tree might be empty (rank = -1).
	    * postcondition: none
	    */
   
	//complexity O(logn)
	public int join(IAVLNode x, AVLTree t)
	{
		int thisRank = this.getRoot().getHeight();
		int tRank = t.getRoot().getHeight();
		int thisSize = this.size;
		int tSize = t.size;
		int complexity = (Math.abs(thisRank-tRank)+1);
		IAVLNode fakeX = new AVLNode(x.getKey(), x.getValue());
		AVLTree t1;
		AVLTree t2;
		if(t.empty()==true) 
		   {//if the tree we get is empty
			   this.insert(x.getKey(), x.getValue());
			   return this.getRoot().getHeight()+1;
		   }
		   if(this.empty())
		   {//if our tree is empty
			   t.insert(x.getKey(), x.getValue());
			   this.root = t.getRoot();
			   this.size = t.size;
			   return this.getRoot().getHeight()+1;
		   }
		if(thisRank>tRank) //this is the taller tree 
		{
			t1 = t;
			t2 = this;
		}
		else 
		{
			t1 = this;
			t2 = t;
		}	
		if(thisRank==tRank) 
		{
			IAVLNode b = t2.getRoot();
			IAVLNode a = t1.getRoot();
			b.setParent(x);
			a.setParent(x);
			if(b.getKey()>a.getKey()) 
			{
				x.setLeft(a);
				x.setRight(b);
			}
			else 
			{
				x.setLeft(b);
				x.setRight(a);
			}
			x.setSize(tSize+thisSize+1);
			this.root=x;
			this.size=x.getSize();
			min_Avl= findMinimum(this.root);//update min
			max_Avl=findMaximum(this.root);//update max
			updateHeight(x);
			return complexity;
		}
		IAVLNode b = t2.getRoot();
		IAVLNode a = t1.getRoot();
		if(b.getKey()>a.getKey()) ///climbing down t2 from left 
		{
			while(b.getHeight()>a.getHeight())
			{
				b = b.getLeft();
			}
			IAVLNode c = b.getParent();
			if(c!=null) 
			{
				c.setLeft(fakeX);
			}			
			fakeX.setParent(c);
			fakeX.setRight(b);
			fakeX.setLeft(a);
			b.setParent(fakeX);
			a.setParent(fakeX);
			fakeX.setHeight(a.getHeight()+1);
			int counter=0;
			t2.Rebalance(fakeX,counter);
			this.root=t2.getRoot();
			this.size=(tSize+thisSize+1);
		}
		else ///climbing down t2 from right
		{
			while(b.getHeight()>a.getHeight())
			{
				b = b.getRight();
			}
			IAVLNode c = b.getParent();
			if(c!=null) 
			{
				c.setRight(fakeX);
			}
			else 
			{
				t2.root=fakeX;
			}
			fakeX.setParent(c);
			fakeX.setLeft(b);
			fakeX.setRight(a);
			b.setParent(fakeX);
			a.setParent(fakeX);
			fakeX.setHeight(a.getHeight()+1);
			int counter=0;
			t2.Rebalance(fakeX,counter);
			this.root=t2.getRoot();
			this.size=(tSize+thisSize+1);
		}
		min_Avl= findMinimum(this.root);//update min
		max_Avl=findMaximum(this.root);//update max
		updateHeight(x);
		return complexity;
	}


	/**
	   * public interface IAVLNode
	   * ! Do not delete or modify this - otherwise all tests will fail !
	   */
	public interface IAVLNode
	{
		public int getKey(); //returns node's key (for virtuval node return -1)
		public String getValue(); //returns node's value [info] (for virtuval node return null)
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node
    	public void setHeight(int height); // sets the height of the node
    	public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
    	public int getSize();
    	public void setSize(int size);
	}

   /**
   * public class AVLNode
   *
   * If you wish to implement classes other than AVLTree
   * (for example AVLNode), do it in this file, not in 
   * another file.
   * This class can and must be modified.
   * (It must implement IAVLNode)
   */
  public class AVLNode extends AVLTree implements IAVLNode 
  {
	  protected int size;
	  protected String info;
	  protected int key;
	  protected IAVLNode parent;
	  protected IAVLNode Left;
	  protected IAVLNode Right;
	  protected int height;

	  public AVLNode(int key, String info) 
	  {
		  this.size=1;
		  this.info = info;
		  this.key = key; 
		  this.height=0;
		  this.parent = null; 
		  this.Left=new FakeAVLNode();
		  this.Right=new FakeAVLNode();
		  this.Left.setParent(this);
		  this.Right.setParent(this);
	  }//all the following methods are of complexity O(1):
	  public int getSize() {
		  return this.size;
	  }
	  public void setSize(int s) {
		  this.size=s;
	  }
	  
	  public int getKey(){
			return this.key; 
		}
		public String getValue()
		{
			return this.info;
		}
		public void setLeft(IAVLNode node)
		{
			Left = node;
			return; 
		}
		public IAVLNode getLeft()
		{
			return this.Left; 
		}
		public void setRight(IAVLNode node)
		{
			Right = node;
			return; 
		}
		public IAVLNode getRight()
		{
			return this.Right; 
		}
		public void setParent(IAVLNode node)
		{
			this.parent = node; 
		}
		public IAVLNode getParent()
		{
			return this.parent;
		}
		// Returns True if this is a non-virtual AVL node
		public boolean isRealNode()
		{
			if (this.key==-1 || this==null) 
			{
				return false;
			}
			return true;
		}
    public void setHeight(int height)
    {
      this.height=height;
    }
    public int getHeight()
    {
    	if(!isRealNode()) 
    	{
    		return -1;
    	}
      return this.height;
    }
  }
  
  
  /////fake node class:
  public class FakeAVLNode implements IAVLNode 
  {
	  protected int size;
	  protected String info;
	  protected int key;
	  protected IAVLNode parent;
	  protected IAVLNode Left;
	  protected IAVLNode Right;
	  protected int height;

	  public FakeAVLNode() 
	  {
		  this.size=0;
		  this.info = null;
		  this.key = -1; 
		  this.Left=null;
		  this.Right=null;
		  this.height=-1;
	  }
	  public int getSize() {
		  return this.size;
	  }
	  public void setSize(int s) {
		  this.size=s;
	  }
	  public int getKey()
	  {
			return -1; 
	  }
		public String getValue()
		{
			return this.info;
		}
		public void setLeft(IAVLNode node)
		{
			Left = node;
			return;
		}
		public IAVLNode getLeft()
		{
			return this.Left; 
		}
		public void setRight(IAVLNode node)
		{
			Right = node;
			return; 
		}
		public IAVLNode getRight()
		{
			return this.Right;
		}
		public void setParent(IAVLNode node)
		{
			this.parent = node; 
		}
		public IAVLNode getParent()
		{
			return this.parent;
		}
		// Returns True if this is a non-virtual AVL node
		public boolean isRealNode()
		{
			return false;
		}
    public void setHeight(int height)
    {
      this.height=height;
    }
    public int getHeight()
    {    	
    	return -1;
    }
  }
  
}
  

