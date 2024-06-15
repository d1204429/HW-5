import java.util.List;

public class Main {

  public static void main(String[] args) {
    try {
      StockProcessor stockProcessor = new StockProcessor();
      List<String> fileName = stockProcessor.process("C:\\Users\\chias\\IdeaProjects\\HW-5\\src\\STOCK_DAY_ALL_20240613.csv");
      if(fileName.isEmpty()){
        System.out.println("發生錯誤");
      }else{
        for(String path : fileName){
          System.out.println(path);
        }
      }
    }catch (Exception e){
      System.out.println(e.getMessage());
    }

  }
}
