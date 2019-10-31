package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.StudentVO;

public class StudentDAO {
	
	//1. 데이터를 입력(insert)
	public int getStudentregiste(StudentVO svo) throws Exception {
		// ② 데이터 처리를 위한 SQL 문
		// 해당된 필드 no부분은 자동으로 증가되므로 필드를 줄 필요가 없음.
		String dml = "insert into schoolchild "
				+ "(no, name, year, ban, gender, korean, english, math, sic, soc, music, total, avg, register, filename)" + " values "
				+ "(null,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?)";

		Connection con = null;
		PreparedStatement pstmt = null;
		int count =0;

		try {
			// ③ DBUtil 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// ④ 입력받은 학생 정보를 처리하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, svo.getName());
			pstmt.setString(2, svo.getLevel());
			pstmt.setString(3, svo.getBan());
			pstmt.setString(4, svo.getGender());
			pstmt.setInt(5, svo.getKorean());
			pstmt.setInt(6, svo.getEnglish());
			pstmt.setInt(7, svo.getMath());
			pstmt.setInt(8, svo.getSic());
			pstmt.setInt(9, svo.getSoc());
			pstmt.setInt(10, svo.getMusic());
			pstmt.setInt(11, svo.getTotal());
			pstmt.setDouble(12, svo.getAvg());
			pstmt.setString(13, svo.getFilename());

			// ⑤ SQL문을 수행후 처리 결과를 얻어옴
			count = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				// ⑥ 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return count;
	}

	//학생 전체 리스트(select)
	public ArrayList<StudentVO> getStudentTotal() {
		ArrayList<StudentVO> list = new ArrayList<StudentVO>();
		String dml = "select * from schoolchild";

		Connection con = null;
		PreparedStatement pstmt = null;
		//데이타베이스 값을 임시로 저장하는 장소 제공하는 객체
		ResultSet rs = null;
		
		StudentVO studentVO = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				studentVO = new StudentVO(rs.getInt(1), rs.getString(2),
						rs.getString(3), rs.getString(4), rs.getString(5),rs.getInt(6),
						rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getInt(10),
						rs.getInt(11),rs.getInt(12), rs.getDouble(13),rs.getString(14),
						rs.getString(15));
				list.add(studentVO);
			}
		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
			}
		}
		return list;
	}

	//학생삭제 기능(delete)
	public void getStudentDelete(int no) throws Exception {
		// ② 데이터 처리를 위한 SQL 문
		String dml = "delete from schoolchild where no = ?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// ③ DBUtil이라는 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// ⑤ SQL문을 수행후 처리 결과를 얻어옴
			pstmt = con.prepareStatement(dml);
			pstmt.setInt(1, no);

			// ⑤ SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				DBUtil.alertDisplay(5, "학생 성공", "학생 삭제 완료.", "학생 삭제 성공!!!");
			} else {
				DBUtil.alertDisplay(1, "학생 삭제", "학생 삭제 실패.", "학생 삭제 실패!!!");
			}
		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				// ⑥ 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
	}
	
	//수정기능(update tableName set 필드명=수정내용 where 조건내용)
	public StudentVO getStudentUpdate(StudentVO svo, int no) throws Exception {
		String dml = "update schoolchild set " + " korean=?, english=?, math=?, sic=?, soc=?, music=?, total=?, avg=?  where no = ?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// ③ DBUtil이라는 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// ④ 수정한 학생 정보를 수정하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(dml);
			pstmt.setInt(1, svo.getKorean());
			pstmt.setInt(2, svo.getEnglish());
			pstmt.setInt(3, svo.getMath());
			pstmt.setInt(4, svo.getSic());
			pstmt.setInt(5, svo.getSoc());
			pstmt.setInt(6, svo.getMusic());
			pstmt.setInt(7, svo.getTotal());
			pstmt.setDouble(8, svo.getAvg());
			pstmt.setDouble(9, svo.getNo());

			// ⑤ SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				DBUtil.alertDisplay(1, "수정완료", svo.getNo()+"수정되었다.", "와! 성공");
			} else {
				DBUtil.alertDisplay(1, "수정실패", svo.getNo()+"수정안됨.", "와! 실패");
				return null;
			}

		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				// ⑥ 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return svo;
	}

	//학생찾기 기능(select * from schoolchild  where name like '%길동%')
	public ArrayList<StudentVO> getStudentCheck(String name) throws Exception {
		String dml = "select * from schoolchild where name like ?";
		ArrayList<StudentVO> list = new ArrayList<StudentVO>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StudentVO retval = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, "%"+name+"%");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				retval= new StudentVO(rs.getInt(1), rs.getString(2),
						rs.getString(3), rs.getString(4), rs.getString(5),rs.getInt(6),
						rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getInt(10),
						rs.getInt(11),rs.getInt(12), rs.getDouble(13),rs.getString(14),
						rs.getString(15));
				list.add(retval);
			}
		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
			}
		}
		return list;
	}
	

}






