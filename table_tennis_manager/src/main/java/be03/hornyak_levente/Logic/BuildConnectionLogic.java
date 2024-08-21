package be03.hornyak_levente.Logic;

import be03.hornyak_levente.Repository.MainRepo;

public class BuildConnectionLogic {
    private MainRepo mainRepo;

    public BuildConnectionLogic(String user, String pw) {
        this.mainRepo = new MainRepo();
        mainRepo.insert_userName(user);
        mainRepo.insert_password(pw);
        mainRepo.set_connection();
    }

    public Boolean build_connection(){
        return mainRepo.test_connection();
    }
    
}
