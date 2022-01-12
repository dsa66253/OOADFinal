package BookingSystem;

import java.util.ArrayList;
import java.util.Dictionary;
public interface ISearchDictionary {
//	parameters: paras: ["uid", "起站/終站", " 標準車廂/商務車廂", "無/靠窗/走道", “一時間搜尋/輸入車次”, "2021-02-02","3"      ,  "1920", "2"     , "3" ]
//	parameters: paras: ["uid", "起迄站"   , " 車廂種類"       , "座位喜好"    , “訂位方式”         , "日期"       ,"幾點出發" , "車次", "全票票數", "大學生票數" ]
//	parameters: paras: {"start", "end", "date"}
//  parameters: paras: {"0110",  "0110", "2021-02-02"}
	
// 
//	return: [index, "train number", "departure time", "arrival time", "travel time"]
	public ArrayList<Dictionary<String, String>> searchCandidate(Dictionary<String, String> input);
}
