package cc.manbu.schoolinfocommunication.bean;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;

import cc.manbu.schoolinfocommunication.httputils.JsonCollectionsOfScoreResonseParser;

/**
 * 成绩
 *
 * @author gongyong2014
 *
 */
@HttpResponse(parser = JsonCollectionsOfScoreResonseParser.class)
public class SR_Score extends SC_SchoolPar implements Serializable {

	public static final long serialVersionUID = -1698665366859967317L;
	// 考次
	public SR_Exam SR_Exam;
	// 考试人
	public R_Student R_Student;
	// 科目
	public R_Subject R_Subject;
	// 分数
	public int Score;
	public int R_StudentId;
	public int R_SubjectId;
	public String SR_ExamName;
	public String R_StudentName;
	public String R_SubjectName;
	public SR_Exam getSR_Exam() {
		return SR_Exam;
	}

	public void setSR_Exam(SR_Exam sR_Exam) {
		SR_Exam = sR_Exam;
	}

	public R_Student getR_Student() {
		return R_Student;
	}

	public void setR_Student(R_Student r_Student) {
		R_Student = r_Student;
	}

	public R_Subject getR_Subject() {
		return R_Subject;
	}

	public void setR_Subject(R_Subject r_Subject) {
		R_Subject = r_Subject;
	}

	public int getScore() {
		return Score;
	}


	public int getR_StudentId() {
		return R_StudentId;
	}

	public void setR_StudentId(int r_StudentId) {
		R_StudentId = r_StudentId;
	}

	public int getR_SubjectId() {
		return R_SubjectId;
	}

	public void setR_SubjectId(int r_SubjectId) {
		R_SubjectId = r_SubjectId;
	}

	public String getSR_ExamName() {
		return SR_ExamName;
	}

	public void setSR_ExamName(String sR_ExamName) {
		SR_ExamName = sR_ExamName;
	}

	public String getR_StudentName() {
		if(R_Student!=null){
			R_StudentName  = R_Student.getS1_Name();
		}
		return R_StudentName;
	}

	public void setR_StudentName(String r_StudentName) {
		R_StudentName = r_StudentName;
	}

	public String getR_SubjectName() {
		return R_SubjectName;
	}

	public void setR_SubjectName(String r_SubjectName) {
		R_SubjectName = r_SubjectName;
	}

	public void setScore(int score) {
		Score = score;
	}



}
