# no-frosted-ice
Disable Frosted Ice Enchant in Minecraft.

## Usage
 ### config.yml
    enabled
    
    default: false
    enable base function of this plugin
    don't allow new frost to form.
  ----
    removeItem 
    
    default: false
    remove armors with frost walker enchantment 
    when players join the server.
    this overrides removeEnchant
   
   ----
    removeEnchant 
    
    default: false
    remove frost walker enchantment from player's armor
    when players join the server.
    
## Commands

    /frostkiller reload         //reload plugin config
    /frostkiller enable         //toggle killer
    /frostkiller smelt          //smelt frosted ice in sender's world 
    /frostkiller smelt <world>  // smelt frosted ice in specified world

all commands requires frostkiller.admin permission (default op). 

when enabled, this plugin will deny all events of creating frosted ice from the enchantment *Frost Walker*.

smelt function will run asynchronously, scan all chunk loaded in the world and replace all frosted ice with water.

smelt is isolated from *enable* . It can be used separately. 

