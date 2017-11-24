package org.asyou.model;

import org.asyou.db.annotation.Table;

import java.util.Date;

/**
 * Created on 17/10/21 15:13 星期六.
 *
 * @author sd
 */
@Table("loan")
public class LoanModel {
    private Long id;
    private String loanName;
    private Date addDate;

    public Long getId() {
        return id;
    }

    public LoanModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getLoanName() {
        return loanName;
    }

    public LoanModel setLoanName(String loanName) {
        this.loanName = loanName;
        return this;
    }

    public Date getAddDate() {
        return addDate;
    }

    public LoanModel setAddDate(Date addDate) {
        this.addDate = addDate;
        return this;
    }
}
