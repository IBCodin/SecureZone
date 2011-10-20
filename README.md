## SecureZone
This is a plugin for bukkit servers to manage zones on the map where players should be restricted to or restricted from.

The presently defined zone type are:
KEEPIN: Keep players in the zone unless they have a zone-specific permission.
KEEPOUT: Keep players out of the zone unless they have a zone-specific permission.

The plugin watches both player movement and player teleports.

As currently implemented, the plugin should prevent a player from teleporting out of a KEEPIN zone 
if they don't have permission, regardless of the source of the teleport. This is primarily due to 
the fact that where the teleport is detected the plugin can't tell who 'ordered' it. 

This plugin requires WorldEdit to be installed as it uses WorldEdit to select the zones to be created.

### Installation
Place the SecureZone.jar (or SecureZone-version.jar) in your plugins directory.

Presently there is no manual configuration required for SecureZone.
The configuration file is used to hold the currently defined protected zones, and will be created as necessay.

### Commands
/securezone: provides help for the commands you have permission to run.
/securezonecreate <zone> [zonetype]: Create the named zone of the optionally specified type.
Currently defined types are KEEPIN and KEEPOUT with KEEPOUT used if no type is specified.
/securezonedelete <zone>: Delete the named zone
/securezonemodify <zone> <zonetype>: Modify an existing zone's zonetype
/securezonelist [world]: List the currently defined zones. 
If the optional world is specified, only zones from that world will be listed.
/securezonevisit <zone>: Transport the sender to the approximate center of the zone.
This was added to help you locate zones if you have forgotten where they lived. 

Each of the above commands is aliased to suport 'sz' in place of 'securezone'.
Each of the 'non-help' commands is also aliased to support 'szX' where X is the
first letter of the command. (For example /szc is aliased to /securezonecreate).

### Permissions
securezone.admin: Parent permission for all commands and unrestricted access to zones
securezone.create: Create zones
securezone.delete: Delete zones
securezone.modify: Modify zones
securezone.list: List zones
securezone.visit: Visit zones
securezone.ignorezones: grants unrestricted access to all zones

Each named zone recognizes a permission with its zone name.
secureszone.ZoneName
For KEEPIN zones this permission is required to leave the zone. 
For KEEPOUT zones this permission is required to enter the zone.

### Building the plugin

Should you be so inclined, the source for the plugin is on GitHub.
The plugin is built with Maven.
I've been using: mvn clean package install
