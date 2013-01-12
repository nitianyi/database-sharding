package com.opensource.orm.sharding.sql.parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.InverseExpression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.Union;
import net.sf.jsqlparser.statement.update.Update;

public class JdbcParameterFinder implements SelectVisitor, FromItemVisitor,
		ExpressionVisitor, ItemsListVisitor {
	private List<JdbcParameter> jdbcParameters;
	private List<EqualsTo> equalsToList;
	Map<JdbcParameter, EqualsTo> equalsMap;

	public JdbcParameterInfos getJdbcParameters(Select select) {
		jdbcParameters = new LinkedList<JdbcParameter>();
		equalsToList = new LinkedList<EqualsTo>();
		equalsMap = new HashMap<JdbcParameter, EqualsTo>();
		select.getSelectBody().accept(this);
		JdbcParameterInfos infos = new JdbcParameterInfos();
		infos.setEqualsToList(equalsToList);
		infos.setJdbcParameters(jdbcParameters);
		for(EqualsTo item:equalsToList){
			equalsMap.put((JdbcParameter) item.getRightExpression(), item);
		}
		infos.setEqualsMap(equalsMap);
		Map<JdbcParameter, Integer> indexMap = new HashMap<JdbcParameter, Integer>();
		for(int i=0;i<jdbcParameters.size();i++){
			indexMap.put(jdbcParameters.get(i), i);
		}
		infos.setIndexMap(indexMap);
		return infos;
	}

	public JdbcParameterInfos getJdbcParameters(Insert insert) {
		jdbcParameters = new LinkedList<JdbcParameter>();
		equalsToList = new LinkedList<EqualsTo>();
		equalsMap = new HashMap<JdbcParameter, EqualsTo>();
		insert.getItemsList().accept(this);
		JdbcParameterInfos infos = new JdbcParameterInfos();
		infos.setEqualsToList(equalsToList);
		infos.setJdbcParameters(jdbcParameters);
		for(EqualsTo item:equalsToList){
			equalsMap.put((JdbcParameter) item.getRightExpression(), item);
		}
		infos.setEqualsMap(equalsMap);
		Map<JdbcParameter, Integer> indexMap = new HashMap<JdbcParameter, Integer>();
		for(int i=0;i<jdbcParameters.size();i++){
			indexMap.put(jdbcParameters.get(i), i);
		}
		infos.setIndexMap(indexMap);
		return infos;
	}
	public JdbcParameterInfos getJdbcParameters(Update update) {
		jdbcParameters = new LinkedList<JdbcParameter>();
		equalsToList = new LinkedList<EqualsTo>();
		equalsMap = new HashMap<JdbcParameter, EqualsTo>();
		if(update.getExpressions()!=null){
			for (Iterator<Expression> joinsIt = update.getExpressions().iterator(); joinsIt
					.hasNext();) {
				Expression expresion =   joinsIt.next();
				expresion.accept(this);
			}
		}
		update.getWhere().accept(this);
		JdbcParameterInfos infos = new JdbcParameterInfos();
		infos.setEqualsToList(equalsToList);
		infos.setJdbcParameters(jdbcParameters);
		for(EqualsTo item:equalsToList){
			equalsMap.put((JdbcParameter) item.getRightExpression(), item);
		}
		infos.setEqualsMap(equalsMap);
		Map<JdbcParameter, Integer> indexMap = new HashMap<JdbcParameter, Integer>();
		for(int i=0;i<jdbcParameters.size();i++){
			indexMap.put(jdbcParameters.get(i), i);
		}
		infos.setIndexMap(indexMap);
		return infos;
	}
	public JdbcParameterInfos getJdbcParameters(Delete delSmt) {
		jdbcParameters = new LinkedList<JdbcParameter>();
		equalsToList = new LinkedList<EqualsTo>();
		equalsMap = new HashMap<JdbcParameter, EqualsTo>(); 
		delSmt.getWhere().accept(this);
		JdbcParameterInfos infos = new JdbcParameterInfos();
		infos.setEqualsToList(equalsToList);
		infos.setJdbcParameters(jdbcParameters);
		for(EqualsTo item:equalsToList){
			equalsMap.put((JdbcParameter) item.getRightExpression(), item);
		}
		infos.setEqualsMap(equalsMap);
		Map<JdbcParameter, Integer> indexMap = new HashMap<JdbcParameter, Integer>();
		for(int i=0;i<jdbcParameters.size();i++){
			indexMap.put(jdbcParameters.get(i), i);
		}
		infos.setIndexMap(indexMap);
		return infos;
	}
	public void visit(PlainSelect plainSelect) {
		plainSelect.getFromItem().accept(this);

		if (plainSelect.getJoins() != null) {
			for (Iterator<Join> joinsIt = plainSelect.getJoins().iterator(); joinsIt
					.hasNext();) {
				Join join =  joinsIt.next();
				join.getRightItem().accept(this);
			}
		}
		if (plainSelect.getWhere() != null)
			plainSelect.getWhere().accept(this);

	}

	public void visit(Union union) {
		for (Iterator<PlainSelect> iter = union.getPlainSelects().iterator(); iter.hasNext();) {
			PlainSelect plainSelect =  iter.next();
			visit(plainSelect);
		}
	}

	public void visit(Table tableName) {
	}

	public void visit(SubSelect subSelect) {
		subSelect.getSelectBody().accept(this);
	}

	public void visit(Addition addition) {
		visitBinaryExpression(addition);
	}

	public void visit(AndExpression andExpression) {
		visitBinaryExpression(andExpression);
	}

	public void visit(Between between) {
		between.getLeftExpression().accept(this);
		between.getBetweenExpressionStart().accept(this);
		between.getBetweenExpressionEnd().accept(this);
	}

	public void visit(Column tableColumn) {
	}

	public void visit(Division division) {
		visitBinaryExpression(division);
	}

	public void visit(DoubleValue doubleValue) {
	}

	public void visit(EqualsTo equalsTo) {
		if (equalsTo.getRightExpression() instanceof JdbcParameter) {
			equalsToList.add(equalsTo);
		}
		visitBinaryExpression(equalsTo);
	}

	public void visit(Function function) {
	}

	public void visit(GreaterThan greaterThan) {
		visitBinaryExpression(greaterThan);
	}

	public void visit(GreaterThanEquals greaterThanEquals) {
		visitBinaryExpression(greaterThanEquals);
	}

	public void visit(InExpression inExpression) {
		inExpression.getLeftExpression().accept(this);
		inExpression.getItemsList().accept(this);
	}

	public void visit(InverseExpression inverseExpression) {
		inverseExpression.getExpression().accept(this);
	}

	public void visit(IsNullExpression isNullExpression) {
	}

	public void visit(JdbcParameter jdbcParameter) {
		jdbcParameters.add(jdbcParameter);
	}

	public void visit(LikeExpression likeExpression) {
		visitBinaryExpression(likeExpression);
	}

	public void visit(ExistsExpression existsExpression) {
		existsExpression.getRightExpression().accept(this);
	}

	public void visit(LongValue longValue) {
	}

	public void visit(MinorThan minorThan) {
		visitBinaryExpression(minorThan);
	}

	public void visit(MinorThanEquals minorThanEquals) {
		visitBinaryExpression(minorThanEquals);
	}

	public void visit(Multiplication multiplication) {
		visitBinaryExpression(multiplication);
	}

	public void visit(NotEqualsTo notEqualsTo) {
		visitBinaryExpression(notEqualsTo);
	}

	public void visit(NullValue nullValue) {
	}

	public void visit(OrExpression orExpression) {
		visitBinaryExpression(orExpression);
	}

	public void visit(Parenthesis parenthesis) {
		parenthesis.getExpression().accept(this);
	}

	public void visit(StringValue stringValue) {
	}

	public void visit(Subtraction subtraction) {
		visitBinaryExpression(subtraction);
	}

	public void visitBinaryExpression(BinaryExpression binaryExpression) {
		binaryExpression.getLeftExpression().accept(this);
		binaryExpression.getRightExpression().accept(this);
	}

	public void visit(ExpressionList expressionList) {
		for (Iterator<Expression> iter = expressionList.getExpressions().iterator(); iter
				.hasNext();) {
			Expression expression =   iter.next();
			expression.accept(this);
		}

	}

	public void visit(DateValue dateValue) {
	}

	public void visit(TimestampValue timestampValue) {
	}

	public void visit(TimeValue timeValue) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sf.jsqlparser.expression.ExpressionVisitor#visit(net.sf.jsqlparser
	 * .expression.CaseExpression)
	 */
	public void visit(CaseExpression caseExpression) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sf.jsqlparser.expression.ExpressionVisitor#visit(net.sf.jsqlparser
	 * .expression.WhenClause)
	 */
	public void visit(WhenClause whenClause) {
		// TODO Auto-generated method stub

	}

	public void visit(AllComparisonExpression allComparisonExpression) {
		allComparisonExpression.GetSubSelect().getSelectBody().accept(this);
	}

	public void visit(AnyComparisonExpression anyComparisonExpression) {
		anyComparisonExpression.GetSubSelect().getSelectBody().accept(this);
	}

	public void visit(SubJoin subjoin) {
		subjoin.getLeft().accept(this);
		subjoin.getJoin().getRightItem().accept(this);
	}

	public void visit(Concat concat) {
		visitBinaryExpression(concat);
	}

	public void visit(Matches matches) {
		visitBinaryExpression(matches);
	}

	public void visit(BitwiseAnd bitwiseAnd) {
		visitBinaryExpression(bitwiseAnd);
	}

	public void visit(BitwiseOr bitwiseOr) {
		visitBinaryExpression(bitwiseOr);
	}

	public void visit(BitwiseXor bitwiseXor) {
		visitBinaryExpression(bitwiseXor);
	}

}
