/*******************************************************************************
 * Copyright (C) 2019 Linux4
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package de.linux4.missilewars.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import de.linux4.missilewars.MissileWars;

import java.util.*;

import static de.linux4.missilewars.game.Game.PlayerTeam.GREEN;
import static de.linux4.missilewars.game.Game.PlayerTeam.RED;

public class MissileCommands {

	private CommandSender console = Bukkit.getConsoleSender();

	public static boolean spawnObject(Game.PlayerTeam team,String objectName, Location location,int maxDistance,boolean force) {
		List<Integer> position = new ArrayList<>(positions.get(objectName));
		String teamName;
		int moveOut;
		switch (team) {
			case GREEN:
				moveOut=-1;
				position.set(2,-position.get(2));
				teamName="green";
				break;
			case RED:
				moveOut=1;
				teamName="red";
				break;
			default:
				return false;
		}
		if(location.getZ()>maxDistance && team==GREEN && MissileWars.getMWConfig().preventMissilesInOwnBase()) return false;
		if(location.getZ()<-maxDistance && team==RED && MissileWars.getMWConfig().preventMissilesInOwnBase()) return false;
		Location rel = new Location(
				location.getWorld(),
				location.getX() + position.get(0),
				location.getY() + position.get(1),
				location.getZ() + position.get(2)
		);
		if(rel.getZ()<-50 && team==GREEN) force=true;
		if(rel.getZ()>50 && team==RED) force=true;
		Block movedOut=rel.clone().add(0,0,moveOut).getBlock();
		if(!force && movedOut.getType()!=Material.AIR) return false;
		return MissileWars.getWorldEditUtil().pasteSchematic(teamName+"_"+objectName, rel, true);
	}
	public static boolean spawnObject(Game.PlayerTeam team, String objectName, World world) {
		return spawnObject(team,objectName,new Location(world,0,0,0),100,true);
	}
	public static Map<String, List<Integer>> positions=new HashMap<>();
	static {
		positions.put("tomahawk", Arrays.asList(0,-3,4));
		positions.put("shieldbuster", Arrays.asList(0,-3,4));
		positions.put("juggernaut", Arrays.asList(0,-3,4));
		positions.put("lightning", Arrays.asList(0,-3,5));
		positions.put("guardian", Arrays.asList(0,-3,4));
		positions.put("shield",Arrays.asList(0,0,0));
		positions.put("win",Arrays.asList(-27,88,-51));
	}
}
