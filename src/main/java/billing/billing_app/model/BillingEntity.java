package billing.billing_app.model;


import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BillingEntity extends BaseModel {

   private String name;
   private String email;
   private String phone;
   private String address;
   private String password;

}
