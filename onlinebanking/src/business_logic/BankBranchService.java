package business_logic;

import java.util.List;

import models.BankBranch;
import utils.ChangesStatus;



public interface BankBranchService {

	List<BankBranch> getAllBankBranches();
	BankBranch getBankBranch(int bank_branch_id);
	ChangesStatus saveBankBranch(BankBranch bankBranch);
}
