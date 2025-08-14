package service.Jwt;

public interface JwtService {

    public String generateToken(String email,String name,String role,Integer User_id) ;

}
