package DomainLayer.AuthenticationAndSecurity;

import org.springframework.stereotype.Service;

@Service
public class AuthenticationAndSecurityFacade {


    private PasswordEncryptor passwordEncryptor;
    private TokensService tokensService;
    private static AuthenticationAndSecurityFacade instance;

    public synchronized static AuthenticationAndSecurityFacade getInstance() {
        if (instance == null) {
            instance = new AuthenticationAndSecurityFacade();
        }
        return instance;
    }
    public AuthenticationAndSecurityFacade() {
        passwordEncryptor = new PasswordEncryptor();
        tokensService = new TokensService();
    }

    public AuthenticationAndSecurityFacade newForTest(){
        instance= new AuthenticationAndSecurityFacade();
        return instance;
    }

    public void generateToken(String memberId){
        tokensService.generateToken(memberId);
    }

    public String getToken(String memberId){
        return tokensService.getToken(memberId);
    }

    public String encodePassword(String password){
        return passwordEncryptor.encryptPassword(password);
    }

    public void removeToken(String memberId){
        tokensService.removeToken(memberId);
    }



    public boolean validateToken(String token){
        return tokensService.validateToken(token);
    }





}
