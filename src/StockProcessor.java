import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.util.Objects;

public class StockProcessor {
  final String BasicPath = "C:\\Users\\chias\\IdeaProjects\\HW-5\\src\\";
  private List<List<String>> datas = new ArrayList<>();
  private List<List<String>> output = new ArrayList<>();
  private List<String> title;
  private List<List<String>> a;
  private List<List<String>> b;


  public List<String> process(String filename){
    List<String> fileResult = new ArrayList<>();
    readCsvFile(filename);

    if(!datas.isEmpty()){
      pretreatedAndPara();
      title = output.getFirst();
      output.removeFirst();

      b = output.stream()
          .filter(i -> i.getFirst().charAt(1)=='0')
          .toList();

      a = output.stream()
          .filter(i -> !b.contains(i))
          .toList();

      fileResult.add(getFile("type_a_top_20.csv"));
      fileResult.add(getFile("type_a_bottom_20.csv")) ;
      fileResult.add(getFile("type_b_top_20.csv")) ;
      fileResult.add(getFile("type_b_bottom_20.csv")) ;

    }else{
      System.out.println("csv檔讀取錯誤");
    }
    return fileResult;
  }

  private void readCsvFile(String fileName){
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] values = line.split(",");
        datas.add(Arrays.asList(values));
      }
    }catch (Exception e){
      System.out.println(e.getMessage());
    }
  }

  private void pretreatedAndPara(){
    try{
      for (int i = 0 ; i<datas.size() ; i++){
        List<String> outItem = datas.get(i);
        List<String> res = new ArrayList<>(outItem);
        if(i==0){
          res.add("漲跌幅");
        }else{
          res = new ArrayList<>(addZero(outItem)) ;
          double percentage = itemToDouble(res.get(8))/(itemToDouble(res.get(7))-itemToDouble(res.get(8)));
          res.add("\""+percentage*100+"\"");
        }
        output.add(res);
      }
    }catch (Exception e){
      //System.out.println(e.toString());
      e.printStackTrace();
    }
  }

  private List<String> addZero(List<String> input){
    for(String s :input){
      if(s.replace('"',' ').trim().isEmpty()){
        input.set(input.indexOf(s), "\"0\"");
      }
    }
    return input;
  }

  private String getFile(String fileName){
    try
    {
      ComparatorsCus cus = new ComparatorsCus();
      String path = BasicPath+fileName;
      switch (fileName){
        case "type_a_top_20.csv":
          List<List<String>> aSortIncrease = new ArrayList<>(a);
          //Collections.sort(aSortIncrease, cus.LargeToSmall);
          aSortIncrease.sort(cus.LargeToSmall);
          aSortIncrease = new ArrayList<>(DoubleFormat(aSortIncrease));
          writeCsvFile(aSortIncrease, path);
          break;
        case "type_a_bottom_20.csv":
          List<List<String>> aSortDecrease = new ArrayList<>(a);
          aSortDecrease.sort(cus.smallToLarge);
          aSortDecrease = new ArrayList<>(DoubleFormat(aSortDecrease));
          writeCsvFile(aSortDecrease, path);
        case "type_b_top_20.csv":
          List<List<String>> bSortIncrease = new ArrayList<>(b);
          bSortIncrease.sort(cus.smallToLarge);
          bSortIncrease = new ArrayList<>(DoubleFormat(bSortIncrease));
          writeCsvFile(bSortIncrease, path);
        case "type_b_bottom_20.csv":
          List<List<String>> bSortDecrease = new ArrayList<>(b);
          bSortDecrease.sort(cus.smallToLarge);
          bSortDecrease = new ArrayList<>(DoubleFormat(bSortDecrease));
          writeCsvFile(bSortDecrease, path);

      }
      return path;

    }catch (Exception e){
      e.printStackTrace();
      return "";
    }
  }

  private void writeCsvFile(List<List<String>> res, String fileName){
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));//檔案輸出路徑
      res.addFirst(title);
      for (List<String> item : res) {
        bw.newLine();//新起一行
        StringBuilder print = new StringBuilder();
        for (String a : item) {
          print.append(a);
          if (item.indexOf(a) != item.size() - 1) {
            print.append(",");
          }
        }
        bw.write(print.toString());
      }
      bw.close();
    } catch (IOException e) {
      System.out.println(e.toString());
    }
  }

  private List<List<String>> DoubleFormat(List<List<String>> input){
    List<List<String>> res = new ArrayList<>();
    for(int i=0; i<20;i++){
      List<String> item = input.get(i);
      item.set(10, "\""+String.format( "%.2f", itemToDouble(item.get(10)))+"\"");
      res.add(item);
    }
    return res;
  }

  private double itemToDouble(String item){
    if(Objects.equals(item, "\"NaN\""))
      item = "\"0\"";
    return Double.parseDouble(item.replace('"',' ').trim());
  }

  class ComparatorsCus{
    public final Comparator<List<String>> smallToLarge = (List<String> o1, List<String> o2) -> {
      double a= itemToDouble(o1.get(10));
      double b=itemToDouble(o2.get(10));
      return Double.compare(a,b);
    };
    public final Comparator<List<String>> LargeToSmall = (List<String> o1, List<String> o2) -> {
      double a= itemToDouble(o1.get(10));
      double b=itemToDouble(o2.get(10));
      return Double.compare(b,a);
    };
  }
}
