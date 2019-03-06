<%@page import="com.wonders.fzb.base.beans.Page"%>
<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
	<h4 class="modal-title">发布草案</h4>
</div>
<div class="modal-body">
	<form id="system_user_form_table" class="form-horizontal"
		  novalidate="novalidate">
		<div class="form-body">
			<div class="form-group">
				<label class="col-sm-3 control-label">法规规章草案：</label>
				<div class="col-sm-8">
					<input type="text" id="docName" name="docName" class="form-control">

				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label">备注：</label>
				<div class="col-sm-8">
					<textarea id="stComent" name="stComent" class="form-control"
					></textarea>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-4 col-sm-offset-4">
					<input type="button" class="btn btn-w-m btn-success" id="btnSave"
						   name="btnSave" value="提交"> &nbsp;&nbsp;
					<input type="button" class="btn btn-w-m btn-success" data-dismiss="modal" value="返回">
				</div>
			</div>
			<div class="form-group">
				<label class="control-label">起草材料
				</label>
			</div>
            <div class="form-group">
                <table class="table table-striped table-bordered table-hover"
                       data-toggle="table"
                       data-mobile-responsive="true"
                       data-card-view = "true"
                       data-pagination="true">
                    <thead>
                    <tr class="text-center">
                        <th class="text-center" data-field="id">文件类型</th>
                        <th class="text-center" data-field="district_name">文件名称</th>
                        <th class="text-center" data-field="set">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                        <tr class="text-center">
                            <td class="text-left">草案文本<span style="color: red">(必须上传)</span><span style="color: dodgerblue">(范本)</span></td>
                            <td ><span style="color: red">暂未上传</span></td>
                            <td ><label class="btn btn-w-m btn-success">点击上传</label></td>
                        </tr>
                        <tr class="text-center">
                            <td class="text-left">报请审核规章草案的函<span style="color: red">(必须上传)</span><span style="color: dodgerblue">(范本)</span></td>
                            <td ><span style="color: red">暂未上传</span></td>
                            <td ><label class="btn btn-w-m btn-success">点击上传</label></td>
                        </tr>
                        <tr class="text-center">
                            <td class="text-left">起草说明<span style="color: red">(必须上传)</span><span style="color: dodgerblue">(范本)</span></td>
                            <td ><span style="color: red">暂未上传</span></td>
                            <td ><label class="btn btn-w-m btn-success">点击上传</label></td>
                        </tr>
                        <tr class="text-center">
                            <td class="text-left">拟定规章所依据的法律、法规目录<span style="color: red">(必须上传)</span><span style="color: dodgerblue">(范本)</span></td>
                            <td ><span style="color: red">暂未上传</span></td>
                            <td ><label class="btn btn-w-m btn-success">点击上传</label></td>
                        </tr>
                        <tr class="text-center">
                            <td class="text-left">征求各方面意见的情况和材料<span style="color: red">(必须上传)</span><span style="color: dodgerblue">(范本)</span></td>
                            <td ><span style="color: red">暂未上传</span></td>
                            <td ><label class="btn btn-w-m btn-success">点击上传</label></td>
                        </tr>
                        <tr class="text-center">
                            <td class="text-left">参与起草工作的专家对规章草案的意见<span style="color: red">(必须上传)</span><span style="color: dodgerblue">(范本)</span></td>
                            <td ><span style="color: red">暂未上传</span></td>
                            <td ><label class="btn btn-w-m btn-success">点击上传</label></td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="form-group">
                <label class="control-label col-sm-3">起草其他材料
                </label>
                <label class="control-label col-sm-3 btn btn-w-m btn-success">点击上传
                </label>
            </div>
            <div class="form-group">
                <table class="table table-striped table-hover"
                       data-toggle="table"
                       data-mobile-responsive="true"
                       data-card-view = "true"
                       data-pagination="true">
                    <thead>
                    <tr class="text-center">
                        <th class="text-center" data-field="id">文件类型</th>
                        <th class="text-center" data-field="district_name">文件名称</th>
                        <th class="text-center" data-field="set">操作</th>
                    </tr>
                    </thead>
                    <tbody id="otherMaterial">

                    </tbody>
                </table>
            </div>
		</div>
	</form>

</div>
