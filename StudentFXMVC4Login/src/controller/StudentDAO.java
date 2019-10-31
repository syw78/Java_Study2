package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.StudentVO;

public class StudentDAO {
	
	//1. �����͸� �Է�(insert)
	public int getStudentregiste(StudentVO svo) throws Exception {
		// �� ������ ó���� ���� SQL ��
		// �ش�� �ʵ� no�κ��� �ڵ����� �����ǹǷ� �ʵ带 �� �ʿ䰡 ����.
		String dml = "insert into schoolchild "
				+ "(no, name, year, ban, gender, korean, english, math, sic, soc, music, total, avg, register, filename)" + " values "
				+ "(null,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?)";

		Connection con = null;
		PreparedStatement pstmt = null;
		int count =0;

		try {
			// �� DBUtil Ŭ������ getConnection( )�޼���� �����ͺ��̽��� ����
			con = DBUtil.getConnection();

			// �� �Է¹��� �л� ������ ó���ϱ� ���Ͽ� SQL������ ����
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

			// �� SQL���� ������ ó�� ����� ����
			count = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				// �� �����ͺ��̽����� ���ῡ ���Ǿ��� ������Ʈ�� ����
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return count;
	}

	//�л� ��ü ����Ʈ(select)
	public ArrayList<StudentVO> getStudentTotal() {
		ArrayList<StudentVO> list = new ArrayList<StudentVO>();
		String dml = "select * from schoolchild";

		Connection con = null;
		PreparedStatement pstmt = null;
		//����Ÿ���̽� ���� �ӽ÷� �����ϴ� ��� �����ϴ� ��ü
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

	//�л����� ���(delete)
	public void getStudentDelete(int no) throws Exception {
		// �� ������ ó���� ���� SQL ��
		String dml = "delete from schoolchild where no = ?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// �� DBUtil�̶�� Ŭ������ getConnection( )�޼���� �����ͺ��̽��� ����
			con = DBUtil.getConnection();

			// �� SQL���� ������ ó�� ����� ����
			pstmt = con.prepareStatement(dml);
			pstmt.setInt(1, no);

			// �� SQL���� ������ ó�� ����� ����
			int i = pstmt.executeUpdate();

			if (i == 1) {
				DBUtil.alertDisplay(5, "�л� ����", "�л� ���� �Ϸ�.", "�л� ���� ����!!!");
			} else {
				DBUtil.alertDisplay(1, "�л� ����", "�л� ���� ����.", "�л� ���� ����!!!");
			}
		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				// �� �����ͺ��̽����� ���ῡ ���Ǿ��� ������Ʈ�� ����
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
	}
	
	//�������(update tableName set �ʵ��=�������� where ���ǳ���)
	public StudentVO getStudentUpdate(StudentVO svo, int no) throws Exception {
		String dml = "update schoolchild set " + " korean=?, english=?, math=?, sic=?, soc=?, music=?, total=?, avg=?  where no = ?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// �� DBUtil�̶�� Ŭ������ getConnection( )�޼���� �����ͺ��̽��� ����
			con = DBUtil.getConnection();

			// �� ������ �л� ������ �����ϱ� ���Ͽ� SQL������ ����
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

			// �� SQL���� ������ ó�� ����� ����
			int i = pstmt.executeUpdate();

			if (i == 1) {
				DBUtil.alertDisplay(1, "�����Ϸ�", svo.getNo()+"�����Ǿ���.", "��! ����");
			} else {
				DBUtil.alertDisplay(1, "��������", svo.getNo()+"�����ȵ�.", "��! ����");
				return null;
			}

		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				// �� �����ͺ��̽����� ���ῡ ���Ǿ��� ������Ʈ�� ����
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return svo;
	}

	//�л�ã�� ���(select * from schoolchild  where name like '%�浿%')
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






