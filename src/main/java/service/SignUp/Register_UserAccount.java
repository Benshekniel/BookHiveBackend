package service.SignUp;

import model.dto.BookStore.NewBookStoreDTO;
import model.dto.OrgDTO;
import model.dto.UsersDto;

public interface Register_UserAccount {

    String createUser(UsersDto usersDto);

    String createBookStore(NewBookStoreDTO newBookStoreDTO);

    public void updateBookStoreIdByEmail(String email);

}
