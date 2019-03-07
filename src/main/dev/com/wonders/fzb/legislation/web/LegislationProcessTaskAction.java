package com.wonders.fzb.legislation.web;

import com.wonders.fzb.base.actions.BaseAction;
import com.wonders.fzb.base.beans.Page;
import com.wonders.fzb.base.exception.FzbDaoException;
import com.wonders.fzb.legislation.beans.LegislationProcessDoc;
import com.wonders.fzb.legislation.beans.LegislationProcessTask;
import com.wonders.fzb.legislation.services.LegislationProcessDocService;
import com.wonders.fzb.legislation.services.LegislationProcessTaskService;
import com.wonders.fzb.simpleflow.beans.WegovSimpleNode;
import com.wonders.fzb.simpleflow.services.WegovSimpleNodeService;
import dm.jdbc.util.StringUtil;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * LegislationProcessTask action接口
 * @author scalffold created by lj
 */
 
@Namespace("/legislationProcessTask")
@Controller
@Scope("prototype")
public class LegislationProcessTaskAction extends BaseAction {

	private static final long serialVersionUID = -5236871814191219582L;
	@Autowired
	@Qualifier("legislationProcessTaskService")
	private LegislationProcessTaskService legislationProcessTaskService;
	
	
	@Autowired
	@Qualifier("wegovSimpleNodeService")
	private WegovSimpleNodeService wegovSimpleNodeService;

	@Autowired
	@Qualifier("legislationProcessDocService")
	private LegislationProcessDocService legislationProcessDocService;

	private int pageNo = 1;
	private int pageSize = 10;
	Page<LegislationProcessDoc> infoPage;

	@Action(value = "legislationProcessTask_add", results = {@Result(name = SUCCESS, location = "/LegislationProcessTask.jsp"), @Result(name = "List", location = "/legislationProcessTask_list.jsp")})
	public String legislationProcessTask_add() throws FzbDaoException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<LegislationProcessTask> legislationProcessTaskList = new ArrayList<LegislationProcessTask>();
		LegislationProcessTask legislationProcessTask = new LegislationProcessTask();
		legislationProcessTaskService.add(legislationProcessTask);
		return SUCCESS;
	}
	
	
	@Action(value = "legislationProcessTask_list", results = { @Result(name = SUCCESS, location = "/legislation/legislationProcessManager_list.jsp")})
	public String choose_plan_list() throws FzbDaoException {
		String stNodeId = request.getParameter("stNodeId");

		request.setAttribute("nodeId", stNodeId);
		request.setAttribute("stTodoNameList", queryButtonInfo(stNodeId));

		return SUCCESS;
	}

	@Action(value = "nextProcess")
	public void nextProcess() throws FzbDaoException {
		String stDocId = request.getParameter("stDocId");
		String stNodeId = request.getParameter("stNodeId");
		List<LegislationProcessTask> list = legislationProcessTaskService.findByHQL("from LegislationProcessTask t where 1=1 and t.stDocId ='"+stDocId+"' and t.stNodeId='"+stNodeId+"'");
		for(LegislationProcessTask legislationProcessTask:list){
			legislationProcessTask.setStTaskStatus("DONE");
			legislationProcessTaskService.update(legislationProcessTask);

			LegislationProcessTask nextLegislationProcessTask = new LegislationProcessTask ();
			nextLegislationProcessTask.setStDocId(legislationProcessTask.getStDocId());
			nextLegislationProcessTask.setStFlowId(legislationProcessTask.getStFlowId());
			List<WegovSimpleNode> nodeList = wegovSimpleNodeService.findByHQL("from WegovSimpleNode t where 1=1 and t.stNodeId ='"+stNodeId+"'");
			nextLegislationProcessTask.setStNodeId(nodeList.get(0).getStNextNode());
			nextLegislationProcessTask.setStNodeName(wegovSimpleNodeService.findByHQL("from WegovSimpleNode t where 1=1 and t.stNodeId ='"+nodeList.get(0).getStNextNode()+"'").get(0).getStNodeName());
			nextLegislationProcessTask.setStTaskStatus("TODO");
			nextLegislationProcessTask.setDtOpenDate(new Date());
			legislationProcessTaskService.add(nextLegislationProcessTask);
			legislationProcessDocService.executeSqlUpdate("update LegislationProcessDoc s set s.stNodeId='"+nextLegislationProcessTask.getStNodeId()+"',s.stNodeName='"+nextLegislationProcessTask.getStNodeName()+"' where s.stDocId='"+nextLegislationProcessTask.getStDocId()+"'");
		}
	}

	@Action(value = "nextChildProcess")
	public void nextChildProcess() throws FzbDaoException {
		String stDocId = request.getParameter("stDocId");
		String stNodeId = request.getParameter("stNodeId");
		List<LegislationProcessTask> list = legislationProcessTaskService.findByHQL("from LegislationProcessTask t where 1=1 and t.stDocId ='"+stDocId+"' and t.stNodeId='"+stNodeId+"'");
		for(LegislationProcessTask legislationProcessTask:list){
			String curStTaskStatus =legislationProcessTask.getStTaskStatus();

			String[] stTaskStatusArray= wegovSimpleNodeService.findByHQL("from WegovSimpleNode t where 1=1 and t.stNodeId ='"+stNodeId+"'").get(0).getStDoneName().split("#");
			for(int i=0;i<stTaskStatusArray.length;i++){
				if(curStTaskStatus.equals(stTaskStatusArray[i])){
					legislationProcessTask.setStTaskStatus(stTaskStatusArray[i+1]);
					break;
				}
			}
			legislationProcessTaskService.update(legislationProcessTask);
		}
	}

	@Action(value = "legislationProcessTask_table", results = { @Result(name = SUCCESS, location = "/legislation/legislationProcessManager_table.jsp")})
	public String choose_plan_table() throws FzbDaoException {
		String pageSize = request.getParameter("pageSize");
		String pageNo = request.getParameter("pageNo");
		String stNodeId = request.getParameter("stNodeId");
		String stUserName=request.getParameter("stUserName");
		String taskStatus=request.getParameter("taskStatus");
		String startTime=request.getParameter("startTime");
		String endTime=request.getParameter("endTime");
		String stDocName=request.getParameter("stDocName");

		System.out.println("查询任务节点stNodeId:"+stNodeId);
		if (null == pageSize || "".equals(pageSize)) {
			pageSize = "10";
		}
		if (null == pageNo || "".equals(pageNo)) {
			pageNo = "1";
		}
		Map<String, Object> condMap = new HashMap<String, Object>();
		Map<String, Object> condMapSub = new HashMap<String, Object>();

		WegovSimpleNode nodeInfo = wegovSimpleNodeService.findById(stNodeId);
		//条件拼装
		String baseSql = "WHERE 1=1 ";

		if(null != stNodeId && !"".equals(stNodeId)){
			baseSql += "and t.st_node_Id = '"+stNodeId+"' ";
		}
		if(null != startTime && !"".equals(startTime)){
			baseSql += "and d.DT_CREATE_DATE >= TO_DATE('"+startTime+"','yyyy-mm-dd')";
		}
		if(StringUtil.isNotEmpty(endTime)){
			baseSql += "and d.DT_CREATE_DATE <= TO_DATE('"+endTime+"','yyyy-mm-dd')";
		}
		if(null != stUserName && !"".equals(stUserName)){
			baseSql += "and d.ST_USER_NAME like '%"+stUserName+"%' ";
		}
		if(null != stDocName && !"".equals(stDocName)){
			baseSql += "and d.st_doc_name like '%"+stDocName+"%' ";
		}
		if(null != taskStatus && !"".equals(taskStatus)){
			baseSql += "and t.st_task_status = '"+taskStatus+"' ";
		}else{
			baseSql += "and t.st_task_status = 'TODO' ";
		}
		//这个环节任务要加入当前人员的处室区分
		if("NOD_0000000101".equals(stNodeId)){
			baseSql += "and t.st_team_Id = '"+session.getAttribute("unitCode")+"' ";
		}


		String orderSql = " order by d.dt_create_date DESC";

		infoPage=legislationProcessTaskService.findTaskDocListByNodeId(baseSql + orderSql, Integer.parseInt(pageNo), Integer.parseInt(pageSize));
		System.out.println("查询到的当前节点草案任务数："+infoPage.getTotalSize());
		System.out.println("查询任务节点stNodeId:"+stNodeId);

		System.out.println("当前节点："+nodeInfo.getStNodeName());
		if(StringUtil.isEmpty(taskStatus)){
			request.setAttribute("buttonStatus", "TODO");
		}else{
			request.setAttribute("buttonStatus", taskStatus);
		}
		request.setAttribute("nodeInfo", nodeInfo);
		request.setAttribute("pageNo", pageNo);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("retPage", infoPage);
		request.setAttribute("nodeId", stNodeId);
		return SUCCESS;
	}

	public Page<LegislationProcessDoc> getInfoPage() {
		return infoPage;
	}


	public void setInfoPage(Page<LegislationProcessDoc> infoPage) {
		this.infoPage = infoPage;
	}

	/**
	 * 查询buttonInfo
	 * @param stNodeId
	 * @return
	 */
	private List<Object> queryButtonInfo(String stNodeId){
		List<Object> buttonNameList =new ArrayList<>();
		HashMap<String,String> nameMap = null;

		List<WegovSimpleNode> nodeList = wegovSimpleNodeService.findByHQL("from WegovSimpleNode t where 1=1 and t.stNodeId ='"+stNodeId+"'");
		if(nodeList!=null && nodeList.size()>0){
			WegovSimpleNode wegovSimpleNode = nodeList.get(0);
			if(StringUtil.isNotEmpty(wegovSimpleNode.getStTodoName())){
				String[] stTodoNameArray= wegovSimpleNode.getStTodoName().split("#");
				String[] stDoneNameArray= wegovSimpleNode.getStDoneName().split("#");
				for(int i=0;i<stTodoNameArray.length;i++){
					nameMap = new HashMap<>();
					nameMap.put("buttonName",stTodoNameArray[i]);
					nameMap.put("buttonId",stDoneNameArray[i]);
					if(i==0){
						nameMap.put("buttonClass","btn btn-w-m btn-success");
					}else{
						nameMap.put("buttonClass","btn btn-w-m btn-default");
					}
					buttonNameList.add(nameMap);
				}
			}
		}
		return buttonNameList;
	}

}
