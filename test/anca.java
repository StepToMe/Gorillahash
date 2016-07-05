public static double[][] computeSimilarities(List<int[]> x){ 
  int N = x.size();

  int[] p;
  int[] q;
  double s;

  int op;

  boolean[][] cells = new boolean[N][N];
  int c = 0;
  int skipped = 0;
  int limit = N*N;
  double[][] result = new double[N][N];
  
  for(int w = 0; w < N; w++){
   for(int y = 0; y < N; y++){
    if(c == limit)
     break;
    if(!cells[y][y]){
     result[y][y] = 1;
     cells[y][y] = true;
     c++;
    }
   }

  }
  
  for(int row = 0; row < N; row++){
   if(c == limit)
    break;
   if(!cells[row][row]){
    result[row][row] = 1;
    cells[row][row] = true;
    c++;
   } 
   for(int col = 0; col < N; col++){
    if(cells[row][col]){
     skipped++;
     continue;
    }
     
    //diagonal lefttop - rightbottom
    //same species
    if(!cells[col][col]){
     result[col][col] = 1;
     cells[col][col] = true;
     c++;
    } 
    //diagonal righttop-leftbottom
    op = N-1-col;
    if(!cells[col][op]){
     p = x.get(col);
     q = x.get(op);
     s = cos_angle(p, q);
     result[col][op] = s;
     cells[col][op] = true; 
     c++;
     if(!cells[op][col]){
      result[op][col] = s;
      cells[op][col] = true;
      c++;
     }
    }
    //last row
    if(!cells[N-1][col]){
     p = x.get(N-1);
     q = x.get(col);
     s = cos_angle(p, q);
     result[N-1][col] = s;
     cells[N-1][col] = true; 
     c++;
     if(!cells[col][N-1]){
      result[col][N-1] = s;
      cells[col][N-1] = true;
      c++;
     }
    }
    if(!cells[row][col]){
     p = x.get(row);
     q = x.get(col);
     s = cos_angle(p, q);
     result[row][col] = s;
     cells[row][col] = true; 
     c++;
     if(!cells[col][row]){      
      result[col][row] = s;
      cells[col][row] = true; 
      c++;
     }
    }

   }   

//   System.out.println("Skipped: " + skipped);
//   System.out.println("Computed: " + c);
//
//   for (int j = 0; j < cells.length; j++) {
//    String value = "";
//    for (int i = 0; i < cells.length; i++) {
//     if(cells[i][j])
//      value = "T";
//     else 
//      value = "F";
//     System.out.print(" " + value + "  ");
//    }
//    System.out.print("\n");
//   }
//   System.out.println();
  }
  System.out.println("Skipped Total: " + skipped);
  System.out.println("Computed Total: " + c);
  return result;
 }