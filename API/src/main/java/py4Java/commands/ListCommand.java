/*
 * Copyright (c) 2010-2019, sikuli.org, sikulix.com - MIT license
 */
package py4Java.commands;

import static py4Java.NetworkUtil.safeReadLine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import py4Java.Protocol;
import py4Java.Py4JException;
import py4Java.ReturnObject;

/**
 * <p>
 * A ListCommand is responsible for handling operations on lists (e.g.,
 * slicing).
 * </p>
 * 
 * @author Barthelemy Dagenais
 * 
 */
public class ListCommand extends AbstractCommand {

	private final Logger logger = Logger.getLogger(ListCommand.class.getName());

	public static final String LIST_COMMAND_NAME = "l";

	public static final char LIST_SORT_SUB_COMMAND_NAME = 's';
	public static final char LIST_REVERSE_SUB_COMMAND_NAME = 'r';
	public static final char LIST_MAX_SUB_COMMAND_NAME = 'x';
	public static final char LIST_MIN_SUB_COMMAND_NAME = 'n';
	public static final char LIST_SLICE_SUB_COMMAND_NAME = 'l';

	public static final char LIST_CONCAT_SUB_COMMAND_NAME = 'a';
	public static final char LIST_MULT_SUB_COMMAND_NAME = 'm';
	public static final char LIST_IMULT_SUB_COMMAND_NAME = 'i';
	public static final char LIST_COUNT_SUB_COMMAND_NAME = 'f';

	public static final String RETURN_VOID = Protocol.RETURN_MESSAGE + "" + Protocol.SUCCESS + "" + Protocol.VOID
			+ Protocol.END_OUTPUT;

	public ListCommand() {
		super();
		this.commandName = LIST_COMMAND_NAME;
	}

	@SuppressWarnings({ "rawtypes" })
	private String call_collections_method(BufferedReader reader, char listCommand) throws IOException {
		String returnCommand;
		String list_id = reader.readLine();

		// Read end of command
		reader.readLine();

		List list = (List) gateway.getObject(list_id);
		try {
			if (listCommand == LIST_SORT_SUB_COMMAND_NAME) {
				returnCommand = sort_list(list);
			} else if (listCommand == LIST_REVERSE_SUB_COMMAND_NAME) {
				returnCommand = reverse_list(list);
			} else if (listCommand == LIST_MAX_SUB_COMMAND_NAME) {
				returnCommand = max_list(list);
			} else if (listCommand == LIST_MIN_SUB_COMMAND_NAME) {
				returnCommand = min_list(list);
			} else {
				returnCommand = Protocol.getOutputErrorCommand();
			}
		} catch (Exception e) {
			returnCommand = Protocol.getOutputErrorCommand();
		}
		return returnCommand;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String concat_list(BufferedReader reader) throws IOException {
		List list1 = (List) gateway.getObject(reader.readLine());
		List list2 = (List) gateway.getObject(reader.readLine());
		// Read end
		reader.readLine();

		List list3 = new ArrayList(list1);
		list3.addAll(list2);
		ReturnObject returnObject = gateway.getReturnObject(list3);
		return Protocol.getOutputCommand(returnObject);
	}

	@SuppressWarnings("rawtypes")
	private String count_list(BufferedReader reader) throws IOException {
		List list1 = (List) gateway.getObject(reader.readLine());
		Object objectToCount = Protocol.getObject(reader.readLine(), gateway);

		// Read end
		reader.readLine();

		int count = Collections.frequency(list1, objectToCount);
		ReturnObject returnObject = gateway.getReturnObject(count);
		return Protocol.getOutputCommand(returnObject);
	}

	@Override
	public void execute(String commandName, BufferedReader reader, BufferedWriter writer)
			throws Py4JException, IOException {
		char subCommand = safeReadLine(reader).charAt(0);
		String returnCommand = null;
		if (subCommand == LIST_SLICE_SUB_COMMAND_NAME) {
			returnCommand = slice_list(reader);
		} else if (subCommand == LIST_CONCAT_SUB_COMMAND_NAME) {
			returnCommand = concat_list(reader);
		} else if (subCommand == LIST_MULT_SUB_COMMAND_NAME) {
			returnCommand = mult_list(reader);
		} else if (subCommand == LIST_IMULT_SUB_COMMAND_NAME) {
			returnCommand = imult_list(reader);
		} else if (subCommand == LIST_COUNT_SUB_COMMAND_NAME) {
			returnCommand = count_list(reader);
		} else {
			returnCommand = call_collections_method(reader, subCommand);
		}

		logger.finest("Returning command: " + returnCommand);
		writer.write(returnCommand);
		writer.flush();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String imult_list(BufferedReader reader) throws IOException {
		List list1 = (List) gateway.getObject(reader.readLine());
		List tempList = new ArrayList(list1.subList(0, list1.size()));
		int n = Protocol.getInteger(reader.readLine());
		// Read end
		reader.readLine();

		if (n <= 0) {
			list1.clear();
		} else {
			for (int i = 1; i < n; i++) {
				list1.addAll(tempList);
			}
		}

		return RETURN_VOID;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String max_list(List list) {
		Object object = Collections.max(list);
		ReturnObject returnObject = gateway.getReturnObject(object);
		return Protocol.getOutputCommand(returnObject);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String min_list(List list) {
		Object object = Collections.min(list);
		ReturnObject returnObject = gateway.getReturnObject(object);
		return Protocol.getOutputCommand(returnObject);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String mult_list(BufferedReader reader) throws IOException {
		List list1 = (List) gateway.getObject(reader.readLine());
		int n = Protocol.getInteger(reader.readLine());
		// Read end
		reader.readLine();

		List list2 = new ArrayList();
		for (int i = 0; i < n; i++) {
			list2.addAll(list1);
		}
		ReturnObject returnObject = gateway.getReturnObject(list2);
		return Protocol.getOutputCommand(returnObject);
	}

	@SuppressWarnings({ "rawtypes" })
	private String reverse_list(List list) {
		Collections.reverse(list);
		return RETURN_VOID;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String slice_list(BufferedReader reader) throws IOException {
		List list1 = (List) gateway.getObject(reader.readLine());
		List<Object> arguments = getArguments(reader);
		List slice = new ArrayList();
		for (Object argument : arguments) {
			slice.add(list1.get((Integer) argument));
		}
		ReturnObject returnObject = gateway.getReturnObject(slice);
		return Protocol.getOutputCommand(returnObject);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String sort_list(List list) {
		Collections.sort(list);
		return RETURN_VOID;
	}

}
