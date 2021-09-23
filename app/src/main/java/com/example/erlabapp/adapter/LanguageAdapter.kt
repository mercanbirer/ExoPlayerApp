package com.example.erlabapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.erlabapp.R
import com.example.erlabapp.databinding.LanguageItemBinding
import com.example.erlabapp.model.Language


class LanguageAdapter(private val context: Context, var datasource: List<Language>) : BaseAdapter() {
	var selectedLanguageName: String = ""

	@SuppressLint("UseCompatLoadingForDrawables")
	override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
		val binding: LanguageItemBinding

		if (convertView == null) {
			binding = LanguageItemBinding.inflate(LayoutInflater.from(context), parent, false)
			binding.root.tag = binding
		} else {
			binding = convertView.tag as LanguageItemBinding
		}

		val language = datasource[position]
		binding.tvMenuTextview.text = language.name

		if(language.name == selectedLanguageName) {
			binding.root.background = context.getDrawable(R.drawable.ic_language_selected)
		}
		else {
			binding.root.background = context.getDrawable(R.drawable.selector_language)
		}

		return binding.root
	}


	override fun getItem(position: Int): Language {
		return datasource[position]
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getCount(): Int {
		return datasource.size
	}


}
