package ra.bussiness;

import ra.entity.Account;
import ra.entity.Bill;
import ra.entity.BillDetail;
import ra.util.ConnectionDB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ShopBussiness {
    public static Account login(String userName, String password) {
        Connection conn = null;
        CallableStatement callSt = null;
        Account acc = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{call loginUser(?,?)}");
            callSt.setString(1, userName);
            callSt.setString(2, password);
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                acc = new Account();
                acc.setId(rs.getInt("acc_id"));
                acc.setUserName(rs.getString("user_name"));
                acc.setPassword(rs.getString("password"));
                acc.setPermission(rs.getBoolean("permission"));
                acc.setEmp_id(rs.getString("emp_id"));
                acc.setStatus(rs.getBoolean("acc_status"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return acc;
    }

    public static int createImportBill(Bill bill) {
        Connection conn = null;
        CallableStatement callSt = null;
        int importBillId = -1;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{call create_import_bill(?,?,?,?)}");
            callSt.setString(1, bill.getBillCode());
            callSt.setBoolean(2, bill.isBillType());
            callSt.setString(3, bill.getEmpIdCreated());
            callSt.registerOutParameter(4, Types.INTEGER);
            callSt.execute();
            importBillId = callSt.getInt(4);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return importBillId;
    }

    public static boolean createImportBillDetail(BillDetail billDetail) {
        Connection conn = null;
        CallableStatement callSt = null;
        boolean result = false;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{call create_import_bill_detail(?,?,?,?)}");
            callSt.setInt(1, billDetail.getBillId());
            callSt.setString(2, billDetail.getProductId());
            callSt.setInt(3, billDetail.getQuantity());
            callSt.setFloat(4, billDetail.getPrice());
            callSt.executeUpdate();
            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return result;
    }

    public static boolean authBillCode(String billCode) {
        //Từ billCode lấy ra các bill chi tiết
        //Duyệt bill chi tiết cộng số lượng vào sản phẩm
        Connection conn = null;
        CallableStatement callSt1 = null;
        CallableStatement callSt2 = null;
        boolean result = false;
        try {
            //Set trạng thái không tự động commit
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            //1. Lấy danh sách các mã phiếu nhập chi tiết
            List<Integer> listBillDetailId = new ArrayList<>();
            callSt1 = conn.prepareCall("{call getBillDetailByBillCode(?)}");
            callSt1.setString(1, billCode);
            ResultSet rs = callSt1.executeQuery();
            while (rs.next()) {
                listBillDetailId.add(rs.getInt("bill_detail_id"));
            }
            //2. Duyệt các phiếu nhập chi tiết
            for (int billDetaiId : listBillDetailId) {
                callSt2 = conn.prepareCall("{call authImportBillDetail(?)}");
                callSt2.setInt(1,billDetaiId);
                int resultAuth = callSt2.executeUpdate();
                if (resultAuth!=1){
                    conn.rollback();
                }
            }
            conn.commit();
            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn, callSt1);
            ConnectionDB.closeConnection(conn,callSt2);
        }
        return result;
    }
}
